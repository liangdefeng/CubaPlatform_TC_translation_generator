# Introduction
A console Spring Boot program to convert Cuba Platform's Simple Chinese translation to Traditional Chinese using opencc4j library.

opencc4j
https://github.com/houbb/opencc4j


# How to run
## Clone the repository
```
git clone https://github.com/liangdefeng/cuba_translation.git
git clone https://github.com/cuba-platform/translations.git

```
Let's say we checkout the translation repository to #TRANSLATION_PATH#

## Change application.properties
Set cuba.platform.translation.content.path to the path of the content folder of the translations repository.
In my case, I checked out the translation repository to C:\\Users\\defen\\IdeaProjects\\translations, so I set it to 
C:\\Users\\defen\\IdeaProjects\\translations\\content 

## Execute the command.
### Windows
```
gradlew.bat bootRun 
```
### Linux
```
./gradlew bootRun 
```

## Check the result
The traditional Chinese translation will be generated in #TRANSLATION_PATH#/content/zh_HK