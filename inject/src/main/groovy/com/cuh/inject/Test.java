package com.cuh.inject;/*
 *
 * Copyright (C) 2022 NIO Inc
 *
 * Ver   Date        Author    Desc
 *
 * V1.0  2022/5/16  hai.cui  Add for
 *
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Test {

//    void processJarV2(File file, File outFile) {
//        CRC32 crc32 = new CRC32();
//        outFile.delete();
//        outFile.getParentFile().mkdirs();
//        FileOutputStream fos = null;
//
//        try {
//            fos = new FileOutputStream(outFile);
//            ZipOutputStream zos = new ZipOutputStream(fos);
//            ZipFile zipFile = null;
//            try {
//                zipFile = new ZipFile(file);
//                ArrayList<? extends ZipEntry> list = Collections.list(zipFile.entries());
//                Iterable<? extends ZipEntry> forEachIv = list;
//                Iterator iterator = forEachIv.iterator();
//                while (iterator.hasNext()) {
//                    ZipEntry it = (ZipEntry) iterator.next();
////            print("ZipEntry-"+it)
//                    String name = it.getName();
//                    InputStream z;
//                    try {
//                        z = zipFile.getInputStream(it);
//                        byte[] bytes = toReadBytes(z)
//
//                        if (name.endsWith(".class")) {
//                            bytes = doTransferV2(name, bytes);
//                        }
//                        crc32.reset();
//                        crc32.update(bytes);
//                        ZipEntry zipEntry = new ZipEntry(name);
//                        zipEntry.setMethod(ZipEntry.STORED);
//                        ;
//                        zipEntry.setSize(bytes.length);
//                        zipEntry.setCompressedSize(bytes.length);
//                        zipEntry.setCrc(crc32.getValue());
//                        zos.putNextEntry(zipEntry);
//                        zos.write(bytes);
//                        zos.closeEntry();
//                    } catch (Exception e1) {
//
//                    } finally {
////                z.close()
////                zipFile.close()
//                    }
//                }
//            } catch (Exception var51) {
//
//            } finally {
//                zipFile.close();
//            }
//
//        } catch (Exception var51) {
//
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
}
