package com.cuh.inject

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import kotlin.Unit
import kotlin.io.ByteStreamsKt
import kotlin.io.CloseableKt
import kotlin.jvm.internal.Intrinsics
import kotlin.text.StringsKt
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarFile
import java.util.logging.FileHandler
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
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        super.transform(context, inputs, referencedInputs, outputProvider, isIncremental)
        print("--------------start transform" + "------isIncremental" + isIncremental + "\n")
        if (true) {
            inputs.each { transformInput ->
                transformInput.jarInputs.each { jarInput ->
                    def jarFile = jarInput.file
                    def status = jarInput.status
                    if (status == Status.REMOVED) {
                        return
                    }
                    def uniqueName = jarFile.name + "_" + MD5.md5String(jarFile.absolutePath)
                    def jarOutDir = outputProvider.getContentLocation(uniqueName, outputTypes, jarInput.scopes, Format.JAR)
//                    println("--------------Status of jar " + jarFile + " is " + status + "jarOutDir " + jarOutDir)
                    processJarFile(jarFile, jarOutDir)
//                    scanJar(jarFile, outputProvider)
//                    print("------" + "jarFile--" + jarFile + "\n" + "------" + "jarOutD" + jarOutDir + "\n")
                }
//                transformInput.directoryInputs.each { directoryInput ->
//                    def pathBitLen = directoryInput.file.toString().length() + 1
//                    directoryInput.changedFiles.each {
//                        def classPath = it.toString().substring(pathBitLen)
//                        if (status == Status.NOTCHANGED) {
//                            return
//                        }
//                        println("--------------Status of file" + classPath + "\n")
//                    }
//                }
            }
        }
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
                Unit var54 = Unit.INSTANCE;
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

    private void scanJar(File file, TransformOutputProvider outputProvider) {
        def jarFile = new JarFile(file)
        def jarEntries = jarFile.entries()
        def classReader = new ClassReader(file.bytes)
        print("---" + file.bytes)
        def classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        def classVisitor = new InjectClassClassVisitor(Opcodes.ASM7, classWriter)
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        def bytes = classWriter.toByteArray()
        def fos = new FileOutputStream(file.parentFile.absolutePath + File.separator + name)
        fos.write(bytes)
        fos.close()

        def dest = outputProvider.getContentLocation(
                file.name,
                file.contentTypes,
                file.scopes,
                Format.JAR)
        //这里执行字节码的注入，不操作字节码的话也要将输入路径拷贝到输出路径
//        copyFile(file.file, dest)
//        while (jarEntries.hasMoreElements()) {
//            def jarNext = jarEntries.nextElement()
//            def entryName = jarNext.name
//            print("------entryName-----" + entryName + "\n" + "file---" + file + "\n")
//        }
//        if (jarFile != null) {
//            print("----" + "jarFile-close" + "\n")
//            jarFile.close()
//        }
    }

    private static boolean copyFile(File srcFile, File destFile) {
        try {
            InputStream streamFrom = new FileInputStream(srcFile);
            OutputStream streamTo = new FileOutputStream(destFile);
            def buffer = new byte[1024];
            int len;
            while ((len = streamFrom.read(buffer)) > 0) {
                streamTo.write(buffer, 0, len);
            }
            streamFrom.close();
            streamTo.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
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
        return "aaa-transform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    boolean isIncremental() {
        return true
    }

}