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

For example, I cloned the translation repository to C:\\Users\\defen\\IdeaProjects\\translations, the cuba.platform.translation.content.path I set is  
C:\\Users\\defen\\IdeaProjects\\translations\\content 

## Execute the command.
After changing application.properties, run the commands below.

### Windows
```
gradlew.bat bootRun 
```
### Linux
```
./gradlew bootRun 
```

## Check the result
The traditional Chinese translation can be found in #TRANSLATION_PATH#/content/zh_HK folder.