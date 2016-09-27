package ru.levelp;

import ru.levelp.file_utils.FileCompressor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Olga on 23.09.2016.
 */
public class Main {
    static FileCompressor compressor = new FileCompressor();
    static File backupDir = new File("/Users/Olga/Desktop/backups");
    static String myFile="/Users/Olga/Desktop/testParent/test/c.jpg";

    public static void menu(){
        System.out.println("Система удаленного хранения бэкапов файлов и папок"+"\n");
        System.out.println("Выберите пункт меню.");
        System.out.println("1 - Сделать бэкап");
        System.out.println("2 - Получить бэкап");
        System.out.println("3 - Получить список бэкапов сделанных в период с даты Х по текущее время");
        System.out.println("4 - Выход");
        systemStart();
    }
    public static void systemStart() {
        Scanner scanner = new Scanner(System.in);
        String entry = scanner.next();

        if(!backupDir.exists()){
            backupDir.mkdir();
        }

        try {
            if (entry.equals("4")) {
                System.out.println("Выход из системы. Good by!");
                System.exit(0);
            } else if (entry.equals("1")) {
                System.out.println("Создать бэкап...");
                // Пользователь указывает путь к файлу/папке
                System.out.println("Укажите путь к файлу/папке");

                Scanner scanner1 = new Scanner(System.in);
                String pathFileToBackup = scanner1.next();
                //Указывается имя бэкапа
                System.out.println("Укажите имя бэкапа");

                Scanner scanner2 = new Scanner(System.in);
                String backupFileName = scanner2.next();
                String pathForAllBackups = backupDir + "/" + backupFileName+".zip";
                // Происходит упакова данных в zip-файл
                compressor.zip(pathFileToBackup,pathForAllBackups);
                systemStart();

            } else if (entry.equals("2")) {
                //Получить бэкап по id бэкапа
                //Пользователь указывет id бэкапа и путь, куда сохранить бэкап
                System.out.println("Указжите путь к бэкапу");
                Scanner scanner1 = new Scanner(System.in);
                String backupFileName =scanner1.next();

                compressor.unZip2(backupFileName);
                systemStart();

            } else if (entry.equals("3")) {
                //Получить список бэкапов сделанных в период с даты Х по текущее время
                System.out.println("Получить список бэкапов сделанных в период с даты Х по текущее время");
                //Пользователь вводит дату в формате dd.MM.yyyy
                System.out.println("Введите дату в формате dd.MM.yyyy");
                Scanner scanner1 = new Scanner(System.in);
                String time = scanner1.next();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                Date date = dateFormat.parse(time);
                String outputString = new SimpleDateFormat("dd.MM.yyyy").format(date);
                System.out.println(outputString.toString());

                System.out.println("Метод в работе......");
                systemStart();
            }
            else{
                System.out.println("Проверьте ввод");
                menu();
            }
        } catch (Exception e) {
            System.out.println("Ошибка:" + e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            menu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
