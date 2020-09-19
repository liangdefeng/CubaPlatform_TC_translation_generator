package com.github.liangdefeng.cuba.translation.service;


import com.github.houbb.opencc4j.util.ZhConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A service aim to
 */
@Service
public class TraditionalChineseConvertService {

    private static String ZH_CN = "zh_CN";
    private static String ZH_HK = "zh_HK";
    private static String CN = "CN";
    private static String HK = "HK";
    private static Logger log = LoggerFactory.getLogger(TraditionalChineseConvertService.class);

    @Value("${cuba.platform.translation.content.path}")
    private String contentPath;

    public boolean process() {

        log.info("start process");

        // Check if the paths are set.
        if (Objects.isNull(contentPath)) {
            log.warn("The Cuba Platform translation path is null.");
            return false;
        }

        Path translationPath = Paths.get(contentPath);
        if (Files.notExists(translationPath)) {
            log.warn("The Cuba Platform translation path doesn't exist.");
            return false;
        }

        // zh_CN's path
        String zhCNPathstr;
        if (contentPath.endsWith(File.separator)) {
            zhCNPathstr = contentPath + ZH_CN + File.separator;
        } else {
            zhCNPathstr = contentPath + File.separator + ZH_CN + File.separator;
        }
        Path zhCNPath = Paths.get(zhCNPathstr);
        if (Files.notExists(zhCNPath)) {
            log.warn(String.format("The %s path does not exist.", zhCNPathstr));
            return false;
        }

        // The zh_HK path
        String zhHKPathstr;
        if (contentPath.endsWith(File.separator)) {
            zhHKPathstr = contentPath + ZH_HK + File.separator;
        } else {
            zhHKPathstr = contentPath + File.separator + ZH_HK + File.separator;
        }

        // Delete folders.
        Path zhHKPath = Paths.get(zhHKPathstr);
        try {
            // Delete all the files in zh_HK path if exists.
            if (Files.exists(zhHKPath)) {
                Files.walk(zhHKPath)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }

            if (Files.notExists(zhHKPath)) {
                Files.createDirectory(zhHKPath);
            }

            // Copy all zh_CN's files to zh_HK folder.
            Files.copy(zhCNPath, zhHKPath, StandardCopyOption.REPLACE_EXISTING);
            Files.walk(zhCNPath).forEach(source -> {
                try {
                    Files.copy(source,
                            zhHKPath.resolve(zhCNPath.relativize(source)),
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    log.error(e.toString());
                }
            });

            // Read the line from source path and convert them to traditional Chinse and write to target file.
            Files.walk(zhHKPath, FileVisitOption.values())
                    .filter(path -> path.toString().endsWith("zh_CN.properties"))
                    .forEach(sourcePath -> {
                        // target files which should be ended with zh_HK.properites.
                        Path targetPath = Paths.get(sourcePath.toAbsolutePath().toString().replaceAll(CN, HK));
                        log.info(String.format("Generating file: %s", translationPath.relativize(targetPath).toString()));
                        try {
                            BufferedWriter writer = Files.newBufferedWriter(targetPath);

                            // Handle the first line
                            Files.lines(sourcePath).findFirst().ifPresent(firstLine -> {
                                String zhHK = ZhConverterUtil.toTraditional(firstLine)
                                        .replaceAll("yyyy-MM-dd", "dd/MM/yyyy")
                                        .replaceAll("YYYY-MM-DD", "DD/MM/YYY");
                                try {
                                    writer.write(zhHK);
                                } catch (IOException e) {
                                    log.error(e.toString());
                                }
                            });

                            // handle the rest lines.
                            Files.lines(sourcePath)
                                    .skip(1)
                                    .forEach(line -> {
                                String zhHK = ZhConverterUtil.toTraditional(line)
                                        .replaceAll("yyyy-MM-dd", "dd/MM/yyyy")
                                        .replaceAll("YYYY-MM-DD", "DD/MM/YYY");
                                try {
                                    writer.newLine();
                                    writer.write(zhHK);
                                } catch (IOException e) {
                                    log.error(e.toString());
                                }
                            });

                            writer.flush();
                            writer.close();

                            // Delete the source file.
                            Files.deleteIfExists(sourcePath);
                        } catch (IOException e) {
                            log.error(e.toString());
                        }
                    });
        } catch (IOException e) {
            log.error(e.toString());
        }

        log.info("Done!");
        return false;
    }
}