SET INTELLIJ_HOME=C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2019.3.2
CALL mvn install:install-file -Dfile="%INTELLIJ_HOME%\lib\javac2.jar" -DgroupId=com.intellij -DartifactId=javac2 -Dversion=17.1.5 -Dpackaging=jar
CALL mvn install:install-file -Dfile="%INTELLIJ_HOME%\lib\asm-all.jar" -DgroupId=com.intellij -DartifactId=asm-all -Dversion=17.1.5 -Dpackaging=jar
CALL mvn install:install-file -Dfile="%INTELLIJ_HOME%\lib\forms_rt.jar" -DgroupId=com.intellij -DartifactId=forms_rt -Dversion=17.1.5 -Dpackaging=jar