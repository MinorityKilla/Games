import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        String savegamesPath = "C://Games/savegames";

        GameProgress progress1 = new GameProgress(100, 3, 10, 124.5);
        GameProgress progress2 = new GameProgress(85, 5, 15, 345.7);
        GameProgress progress3 = new GameProgress(45, 7, 22, 567.8);

        List<String> savedFiles = new ArrayList<>();

        String save1 = savegamesPath + "/save1.dat";
        String save2 = savegamesPath + "/save2.dat";
        String save3 = savegamesPath + "/save3.dat";

        saveGame(save1, progress1);
        savedFiles.add(save1);

        saveGame(save2, progress2);
        savedFiles.add(save2);

        saveGame(save3, progress3);
        savedFiles.add(save3);

        String zipPath = savegamesPath + "/saves.zip";
        zipFiles(zipPath, savedFiles);

        for (String filePath : savedFiles) {
            File file = new File(filePath);
            if (file.delete()) {
                System.out.println("Файл " + filePath + " удален");
            }
        }
    }

    private static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void zipFiles(String zipPath, List<String> filesToZip) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (String filePath : filesToZip) {
                File fileToZip = new File(filePath);
                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zos.write(buffer, 0, length);
                    }

                    zos.closeEntry();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}