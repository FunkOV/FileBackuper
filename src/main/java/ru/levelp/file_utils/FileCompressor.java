
package ru.levelp.file_utils;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileCompressor {


    public void zip(String sourcePath, String targetPath) throws IOException {
        Path path = FileSystems.getDefault().getPath(sourcePath);
        System.out.println(path);
        ArrayList<File> filesToCompress = new ArrayList<>();
        doFileList(path.toFile(), filesToCompress);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetPath));

        for (File f : filesToCompress) {
            out.putNextEntry(new ZipEntry(f.getName())); //file.getName()
            FileInputStream fis = new FileInputStream(f);
            write(fis,out);
        }
        System.out.println("Файл сохранен.");
    }

    private ArrayList<File> doFileList(File file, ArrayList<File> filesToCompress) {
        if (file.isFile()) {
            filesToCompress.add(file);
        } else {
            for (File f : file.listFiles()) {
                doFileList(f, filesToCompress);
            }
        }
        return filesToCompress;
    }

    public void write(InputStream inputStream, OutputStream outputStream)throws IOException{
        byte [] buffer = new byte[2048];
        int len;
        while((len=inputStream.read(buffer))>=0){
            outputStream.write(buffer,0,len);
        }
        inputStream.close();
        outputStream.close();
    }
    //не работает
    public void unzip(String sourceZip, String targetFolder) throws IOException {
        ZipFile zipFile = new ZipFile(sourceZip);
        Enumeration entries = zipFile.entries();

        // Создаем каталог, куда будут распакованы файлы
        File targetFolderFile = new File(targetFolder);
        if (!targetFolderFile.exists()) {
            targetFolderFile.mkdirs();
        }
        // Получаем содержимое ZIP архива

        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            System.out.println(entry.getName() + " " + entry.getClass());
            File file = new File(targetFolder, entry.getName());
            System.out.println(file.getName() + " " + file.getPath() + " "+ file.getClass());
            if (entry.isDirectory()) {
                file.mkdirs();
            } else {
                InputStream inputStream = zipFile.getInputStream(entry);
                System.out.println(file.getAbsolutePath());
                File folders = new File(file.getParent());
                folders.mkdirs();
                if (!file.exists()) {
                    file.createNewFile();
                }
                System.out.println(file.getParent());
                FileOutputStream outputStream = new FileOutputStream(file);
                write(inputStream,outputStream);
            }
        }

        zipFile.close();
    }

    public void unZip2(final String zipFileName) {
        byte[] buffer = new byte[2048];

        // Создаем каталог, куда будут распакованы файлы
        final String dstDirectory = destinationDirectory(zipFileName);
        final File dstDir = new File(dstDirectory);
        if (!dstDir.exists()) {
            dstDir.mkdir();
        }

        try {
            // Получаем содержимое ZIP архива
            final ZipInputStream zis = new ZipInputStream(
                    new FileInputStream(zipFileName));
            ZipEntry ze = zis.getNextEntry();
            String nextFileName;
            while (ze != null) {
                nextFileName = ze.getName();
                File nextFile = new File(dstDirectory + File.separator
                        + nextFileName);
                System.out.println("Распаковываем: "
                        + nextFile.getAbsolutePath());
                // Если мы имеем дело с каталогом - надо его создать. Если
                // этого не сделать, то не будут созданы пустые каталоги
                // архива
                if (ze.isDirectory()) {
                    nextFile.mkdir();
                } else {
                    // Создаем все родительские каталоги
                    new File(nextFile.getParent()).mkdirs();
                    // Записываем содержимое файла
                    try (FileOutputStream fos
                                 = new FileOutputStream(nextFile)) {
                        int length;
                        while((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String destinationDirectory(final String srcZip) {
        return srcZip.substring(0, srcZip.lastIndexOf("."));
    }
}

/*
Прочитать ТЗ
Изучить статью по ссылке http://www.javenue.info/post/35
Enumeration и паттерн Iterator (Iterator в "Паттерны проектирования" Head First)
1) Выделить операцию копирования файла в отдельный метод, согласно статье выше
2) Вывод меню согласно ТЗ
3) Делаем бэкапы по запросу пользователя, но не отправляем на сервер,
    а сохраняем в папке backups
 */



