# Introduction
A tool to generate Cuba Platform's Traditional Chinese translation according to existing Simple Chinese translation using opencc4j, an open source java library which translate Simple Chinese to Traditional Chinse, Or vice versa.

OpenCC is a famous Simple Chinese to Traditional Chinese and Traditional Chinese to Simple Chinese converter. and opencc4j is the Java implementation of OpenCC.

OpenCC
https://github.com/BYVoid/OpenCC

opencc4j
https://github.com/houbb/opencc4j

# How to run
## Clone the repositories
We need to clone two repositories.
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