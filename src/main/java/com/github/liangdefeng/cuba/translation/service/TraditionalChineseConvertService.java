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

@Service
public class TraditionalChineseConvertService {

    private static Logger log = LoggerFactory.getLogger(TraditionalChineseConvertService.class);

    @Value("${cuba.platform.translation.path}")
    private String cubaPlatformTranslationPath;

    @Value("${delete.if.exist}")
    private boolean deleteIfExist;

    public boolean process() {

        log.info("start process");

        // Check if the paths are set.
        if (Objects.isNull(cubaPlatformTranslationPath)) {
            log.warn("The Cuba Platform translation path is null.");
            return false;
        }

        Path translationPath = Paths.get(cubaPlatformTranslationPath);
        if (Files.notExists(translationPath)) {
            log.warn("The Cuba Platform translation path doesn't exist.");
            return false;
        }

        // The zh_CN path
        String zhCNPathstr;
        if (cubaPlatformTranslationPath.endsWith(File.separator)) {
            zhCNPathstr = cubaPlatformTranslationPath + "zh_CN" + File.separator;
        } else {
            zhCNPathstr = cubaPlatformTranslationPath + File.separator + "zh_CN" + File.separator;
        }
        Path zhCNPath = Paths.get(zhCNPathstr);
        if (Files.notExists(zhCNPath)) {
            log.warn(String.format("The %s path does not exist.", zhCNPathstr));
            return false;
        }

        // The zh_HK path
        String zhHKPathstr;
        if (cubaPlatformTranslationPath.endsWith(File.separator)) {
            zhHKPathstr = cubaPlatformTranslationPath + "zh_HK" + File.separator;
        } else {
            zhHKPathstr = cubaPlatformTranslationPath + File.separator + "zh_HK" + File.separator;
        }

        // Delete folders.
        Path zhHKPath = Paths.get(zhHKPathstr);
        try {
            if (Files.exists(zhHKPath)) {
                Files.walk(zhHKPath)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }

            if (Files.notExists(zhHKPath)) {
                Files.createDirectory(zhHKPath);
            }

            Files.copy(zhCNPath, zhHKPath, StandardCopyOption.REPLACE_EXISTING);
            Files.walk(zhCNPath)
                    .forEach(source -> {
                        try {
                            Files.copy(source, zhHKPath.resolve(zhCNPath.relativize(source)),StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            log.error(e.toString());
                        }
                    });
        } catch (IOException e) {
            log.error(e.toString());
        }



        try {
            Files.walk(zhHKPath, FileVisitOption.values())
                    .filter(path -> path.toString().endsWith("CN.properties"))
                    .forEach(readFilePath -> {

                        Path writeFilePath = Paths.get(readFilePath.getParent() + File.separator + readFilePath.getFileName().toString().replace("CN","HK"));
                        try {
                            BufferedWriter writer = Files.newBufferedWriter(writeFilePath);
                            Files.lines(readFilePath).forEach(line -> {
                                String zhHK = ZhConverterUtil.toTraditional(line);
                                try {
                                    writer.write(zhHK);
                                    writer.newLine();
                                } catch (IOException e) {
                                    log.error(e.toString());
                                }
                            });

                            writer.flush();
                            writer.close();
                            Files.deleteIfExists(readFilePath);
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