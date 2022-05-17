package com.cuh.inject

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.cuh.inject.cv.InjectClassClassVisitor
import com.cuh.inject.md5.MD5
import kotlin.io.ByteStreamsKt
import kotlin.io.CloseableKt
import kotlin.io.FilesKt
import kotlin.jvm.internal.Intrinsics
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.zip.CRC32
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class InjectTransform extends Transform {

    Project project;

    InjectTransform(Project project1) {
        this.project = project1
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        def outputProvider = transformInvocation.outputProvider
        def inputs = transformInvocation.inputs
        def outDir = transformInvocation.outputProvider.getContentLocation("inject", outputTypes, scopes, Format.DIRECTORY)
        def isIncremental = transformInvocation.incremental
        def executor = new ThreadPoolExecutor(8, 10, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>())

        if (isIncremental) {
            println("Inject doing incremental build ...")
            inputs.each { transformInput ->
                transformInput.jarInputs.each { jarInput ->
                    def jarFile = jarInput.file
                    def status = jarInput.status
                    if (status == Status.NOTCHANGED) {
                        return
                    }
                    def uniqueName = jarFile.name + "_" + MD5.md5String(jarFile.absolutePath)
                    def jarOutFile = outputProvider.getContentLocation(uniqueName, outputTypes, jarInput.scopes, Format.JAR)
                    if (status == Status.REMOVED) {
                        jarOutFile.delete()
                        return
                    }
//                    println("Inject jarInputs" + jarFile + "\n")
                    executor.execute(new Runnable() {
                        @Override
                        void run() {
                            newProcessJarFile(jarFile, jarOutFile)
                        }
                    })
                }
                transformInput.directoryInputs.each { directoryInput ->
                    def pathBitLen = directoryInput.file.toString().length() + 1
                    for (Map.Entry<File, Status> entry : directoryInput.changedFiles.entrySet()) {
                        def file = entry.getKey()
                        def status = entry.value
                        def classPath = file.toString().substring(pathBitLen)
                        if (status == Status.NOTCHANGED) {
                            return
                        }
                        println("Inject directoryInputs" + classPath + "\n")
                        def outputFile = new File(outDir, classPath)
                        if (status == Status.REMOVED) {
                            outputFile.delete()
                            return
                        }
                        executor.execute(new Runnable() {
                            @Override
                            void run() {
                                processClassFile(classPath, file, outputFile)
                            }
                        })
                    }
                }
            }
        } else {
            println("Inject doing non-incremental build ...")
            outputProvider.deleteAll()
            outDir.mkdirs()
            inputs.each { transformInput ->
                transformInput.jarInputs.each { jarInput ->
                    def jarFile = jarInput.file
                    def uniqueName = jarFile.name + "_" + MD5.md5String(jarFile.absolutePath)
                    def jarOutDir = outputProvider.getContentLocation(uniqueName, outputTypes, jarInput.scopes, Format.JAR)
//                    println("Inject jarInputs" + jarFile + "\n" + "jarOutDir" + jarOutDir)
                    executor.execute(new Runnable() {
                        @Override
                        void run() {
                            processClassFileV2(jarFile, jarOutDir)
                        }
                    })
                }
                transformInput.directoryInputs.each { directoryInput ->
                    def pathBitLen = directoryInput.file.toString().length() + 1
                    def file = directoryInput.getFile()
                    def list = allFiles(file)
                    list.each {
                        if (!it.isDirectory()) {
                            def classPath = it.toString().substring(pathBitLen)
                            def outputFile = new File(outDir, classPath)
//                            print("classPath-" + classPath + "\n" + "outputFile-" + outputFile)
                            executor.execute(new Runnable() {
                                @Override
                                void run() {
                                    try {
                                        print("executor")
                                        processClassFileV2(classPath, it, outputFile)
                                    } catch (Throwable e) {
                                        def wrapped = new RuntimeException("Error occurred when processing $f", e)
                                        exceptionSet.add(wrapped)
                                        print("e" + e.toString())
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
        executor.shutdown()
        executor.awaitTermination(Integer.MAX_VALUE.toLong(), TimeUnit.SECONDS)
    }

    private byte[] toReadBytes(InputStream is) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray()
    }

    void processJarV2(File file, File outFile) {
        CRC32 crc32 = new CRC32();
        outFile.delete();
        outFile.getParentFile().mkdirs();
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(outFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipFile zipFile = null;
            try {
                zipFile = new ZipFile(file);
                ArrayList<? extends ZipEntry> list = Collections.list(zipFile.entries());
                Iterable<? extends ZipEntry> forEachIv = list;
                Iterator iterator = forEachIv.iterator();
                while (iterator.hasNext()) {
                    ZipEntry it = (ZipEntry) iterator.next();
//            print("ZipEntry-"+it)
                    String name = it.getName();
                    InputStream inputStream;
                    try {
                        inputStream = zipFile.getInputStream(it);
                        byte[] bytes = toReadBytes(inputStream)

                        if (name.endsWith(".class")) {
                            bytes = doTransferV2(name, bytes);
                        }
                        crc32.reset();
                        crc32.update(bytes);
                        ZipEntry zipEntry = new ZipEntry(name)
                        zipEntry.setMethod(ZipEntry.STORED)
                        zipEntry.setSize(bytes.length)
                        zipEntry.setCompressedSize(bytes.length)
                        zipEntry.setCrc(crc32.getValue())
                        zos.putNextEntry(zipEntry)
                        zos.write(bytes)
                        zos.closeEntry()
                    } catch (Exception e1) {

                    } finally {
                        inputStream.close()
                    }
                }
            } catch (Exception var51) {

            } finally {
                zipFile.close();
            }

        } catch (Exception var51) {

        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void aa(File file, File outFile) {
        def crc32 = new CRC32()
        outFile.delete()
        outFile.parentFile.mkdirs()
        def fos = new FileOutputStream(outFile)
        def zos = new ZipOutputStream(fos)
        def zipFile = new ZipFile(file)
        def list = Collections.list(zipFile.entries())
        def iterator = list.iterator()
        while (iterator.hasNext()) {
            ZipEntry it = (ZipEntry) iterator.next()
//            print("ZipEntry-"+it)
            def name = it.name
            def z = zipFile.getInputStream(it)
            def bytes = toReadBytes(z)

            if (name.endsWith(".class")) {
                bytes = doTransferV2(name, bytes)
            }
            crc32.reset()
            crc32.update(bytes)
            def zipEntry = new ZipEntry(name)
            zipEntry.method = ZipEntry.STORED
            zipEntry.size = bytes.size().toLong()
            zipEntry.compressedSize = bytes.size().toLong()
            zipEntry.crc = crc32.value
            zos.putNextEntry(zipEntry)
            zos.write(bytes)
            zos.closeEntry()
        }
        zos.flush()
    }

    private void processClassFileV2(String classPath, File file, File outputFile) {
//        print("classPath" + classPath)

        if (file.isDirectory()) {
            return
        }
        outputFile.getParentFile().mkdir()
        def classFileBuffer = FilesKt.readBytes(file)
//        print("classPath" + classPath)

        if (classPath.endsWith(".class")) {
            def fos = new FileOutputStream(file)
//            print("classPath---" + classPath + "---" + "length-" + classFileBuffer.length + "\n")
            fos.write(doTransferV2(classPath, classFileBuffer))
            fos.close()
        }
    }

    private byte[] doTransferV2(String classPath, byte[] input) {
        def className = classPath.substring(0, classPath.lastIndexOf('.')).replace(File.separator, '/')
        def reader = new ClassReader(input)
        def writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS)
        def cv = new InjectClassClassVisitor(Opcodes.ASM7, writer)
        reader.accept(cv, ClassReader.EXPAND_FRAMES)
        return writer.toByteArray()
    }

    private static List<File> allFiles(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return null
        ArrayList<File> fileArrayList = new ArrayList<>();
        for (File f : dir.listFiles()) {
            if (f.isFile()) {
                fileArrayList.add(f)
            } else if (f.isDirectory()) {
                List<File> subFiles = allFiles(f)
                if (subFiles != null) {
                    fileArrayList.addAll(subFiles)
                }
            }
        }
        return fileArrayList
    }

    private void processJarFile(File file, File outFile) {
//        print("-----" + "file-" + file + "outFile-" + outFile + "\n")
        def crc32 = new CRC32()
        outFile.delete()
        outFile.parentFile.mkdirs()
        def var4 = (Closeable) (new ZipOutputStream((OutputStream) (new FileOutputStream(outFile))));
        def var5 = false;
        def var6 = (Throwable) null;
        try {
            def zos = (ZipOutputStream) var4;
            def var8 = false;
            def var9 = (Closeable) (new ZipFile(file));
            def var10 = false;
            def var11 = (Throwable) null;
            try {
                def zipFile = (ZipFile) var9;
                def var13 = false;
                def var10000 = zipFile.entries();
                Intrinsics.checkExpressionValueIsNotNull(var10000, "zipFile.entries()");
                def var14 = (Enumeration) var10000;
                def $i$f$forEach = false;
                def var56 = Collections.list(var14);
                Intrinsics.checkExpressionValueIsNotNull(var56, "java.util.Collections.list(this)");
                def $this$forEach$iv = (Iterable) ((List) var56);
                $i$f$forEach = false;
                def var16 = (Iterator) $this$forEach$iv.iterator();

                while (var16.hasNext()) {
                    Object element$iv = var16.next();
                    ZipEntry it = (ZipEntry) element$iv;
//                    print("--------ZipEntry" + it + "\n")
                    def var19 = false;
                    Intrinsics.checkExpressionValueIsNotNull(it, "it");
                    String name = it.getName();
//                    print("+++++++++.name" + name + "\n")
                    Closeable var22 = (Closeable) zipFile.getInputStream(it);
                    boolean var23 = false;
                    Throwable var24 = (Throwable) null;
                    byte[] var59;
                    try {
                        def it1 = (InputStream) var22;
//                        int var26 = false;
                        Intrinsics.checkExpressionValueIsNotNull(it, "it");
                        var59 = ByteStreamsKt.readBytes(it1);
                    } catch (Throwable var47) {
                        var24 = var47;
                        print("----var47----" + var47)
                        throw var47;
                    } finally {
                        CloseableKt.closeFinally(var22, var24)
                    }

                    byte[] bytes = var59;
                    Intrinsics.checkExpressionValueIsNotNull(name, "name");

                    if (name.endsWith(".class")) {
//                        print("+++++++++class------" + name + "\n")
                        bytes = this.doTransfer(name, var59);
                    }
//                    if (StringsKt.endsWith$default(name, ".class", false, 2, (Object)null)) {
//                        bytes = this.doTransfer(name, var59);
//                    }
                    crc32.reset();
                    crc32.update(bytes);
                    ZipEntry var57 = new ZipEntry(name);
                    boolean var58 = false;
                    boolean var60 = false;
//                    int var28 = false;
                    var57.setMethod(0);
                    var57.setSize((long) bytes.length);
                    var57.setCompressedSize((long) bytes.length);
                    var57.setCrc(crc32.getValue());
                    zos.putNextEntry(var57);
                    zos.write(bytes);
                    zos.closeEntry();
                }
                zos.flush();
                def var54 = Unit.INSTANCE;
            } catch (Throwable var49) {
                var11 = var49;
                print("------var49-----" + var49)
            } finally {
                CloseableKt.closeFinally(var9, var11);
            }
        } catch (Throwable var51) {
            var6 = var51;
            print("------var51---" + var51)
        } finally {
            CloseableKt.closeFinally(var4, var6);
        }
    }

    private final byte[] doTransfer(String classPath, byte[] input) {
        def a = classPath.substring(0, classPath.lastIndexOf("."))
        def className = a.replace(File.separator, "/")
        def reader = new ClassReader(input)
        def writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS)
        def cv = new InjectClassClassVisitor(Opcodes.ASM7, writer)
        reader.accept(cv, ClassReader.EXPAND_FRAMES)
        return writer.toByteArray()
    }
//
//    private static boolean copyFile(File srcFile, File destFile) {
//        try {
//            InputStream streamFrom = new FileInputStream(srcFile);
//            OutputStream streamTo = new FileOutputStream(destFile);
//            def buffer = new byte[1024];
//            int len;
//            while ((len = streamFrom.read(buffer)) > 0) {
//                streamTo.write(buffer, 0, len);
//            }
//            streamFrom.close();
//            streamTo.close();
//            return true;
//        } catch (Exception ex) {
//            return false;
//        }
//    }
//    private void processJarFile(File file, File outFile) {
//        def crc32 = new CRC32()
//        outFile.delete()
//        outFile.parentFile.mkdirs()
//        def fileOutputStream = new FileOutputStream(outFile) as FileOutputStream
//        print("fileOutputStream" + fileOutputStream + "\n")
//        def zipOutputSteam = new ZipOutputStream(fileOutputStream) as ZipOutputStream
//        print("zipOutputSteam" + zipOutputSteam)
//        return
//        zipOutputSteam.use().each { zos ->
////            new ZipFile(file).use { zipFile ->
////                def newZipFile = zipFile as ZipFile
////                def zips = newZipFile.entries().toList() as List<ZipEntry>
////                zips.forEach { it ->
////                    def name = it.name
////                    print("+++++++++"+name)
////                    def bytes = newZipFile.getInputStream(it).use { it.readBytes() }
////                    if (name.endsWith(".class")) {
////                        bytes = doTransfer(name, bytes)
////                    }
////                    crc32.reset()
////                    crc32.update(bytes)
////                    val zipEntry = ZipEntry(name).apply {
////                        method = ZipEntry.STORED
////                        size = bytes.size.toLong()
////                        compressedSize = bytes.size.toLong()
////                        crc = crc32.value
////                    }
////                    zos.putNextEntry(zipEntry)
////                    zos.write(bytes)
////                    zos.closeEntry()
//        }
////                zos.flush()
////    }
//    }
////        ZipOutputStream(FileOutputStream(outFile)).use { zos ->
////            ZipFile(file).use { zipFile ->
////                zipFile.entries().toList().forEach {
////                    val name = it.name
////                    var bytes = zipFile.getInputStream(it).use { it.readBytes() }
////                    if (name.endsWith(".class")) {
////                        bytes = doTransfer(name, bytes)
////                    }
////                    crc32.reset()
////                    crc32.update(bytes)
////                    val zipEntry = ZipEntry(name).apply {
////                        method = ZipEntry.STORED
////                        size = bytes.size.toLong()
////                        compressedSize = bytes.size.toLong()
////                        crc = crc32.value
////                    }
////                    zos.putNextEntry(zipEntry)
////                    zos.write(bytes)
////                    zos.closeEntry()
////                }
////                zos.flush()
////            }
////        }
////    }

    @Override
    String getName() {
        return "cuihai-transform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

}