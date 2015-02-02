call mvn package
call mvn deploy:deploy-file -DgroupId=com.kaisen.common -DartifactId=kaisen.common -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -Dfile=target/kaisen.common-0.0.1-SNAPSHOT.jar -Durl=http://192.168.2.210:10001/nexus/content/repositories/snapshots/ -DrepositoryId=snapshots
@pause