Steps to configure local maven to connect to thoughtfulcraftsmanship.com
===

Set up repositories
===

- Navigate to http://thoughtfulcraftsmanship.com/nexus
- Change nexus password, it starts at admin/admin123
- Add onejar proxy repository
    - remote storage location: http://onejar-maven-plugin.googlecode.com/svn/mavenrepo/
- Add sean hosted repository (change to snapshot)
- Add datomic hosted repository
- make onejar, sean, and datomic repositories public

Only keep latest snapshot
===

- Administration
- Scheduled Tasks
- Add
- Name
    - Only keep most recent snapshot
- Task Type
    - Remove snapshots from repositories
- Repository / Group
    - sean (Repo)
- Minimum Snapshot Count
    - 1
- Snapshot retention
    - 0
- Recurrence
    - Hourly

Force maven to get all dependencies from http://thoughtfulcraftsmanship.com:8081/nexus
===

update settings.xml so we pull from nexus rather than web

    <mirrors>
        <mirror>
            <id>sean</id>
            <name>Sean Shubin's Maven Repository</name>
            <url>http://thoughtfulcraftsmanship.com/nexus/content/groups/public/</url>
            <mirrorOf>*</mirrorOf>
        </mirror>
    </mirrors>

update settings.xml so we can push to nexus

    <servers>
        <server>
            <id>sean</id>
            <username>admin</username>
            <password>********</password>
        </server>
    </servers>

Upload Datomic
===

    wget https://my.datomic.com/downloads/free/0.9.5130
    mv 0.9.5130 datomic-free-0.9.5130.zip
    unzip datomic-free-0.9.5130.zip
    cd datomic-free-0.9.5130/
    mvn deploy:deploy-file -DgroupId=com.datomic -DartifactId=datomic-free -Dfile=datomic-free-0.9.5130.jar -DpomFile=pom.xml -DrepositoryId=sean -Durl=http://thoughtfulcraftsmanship.com/nexus/content/repositories/datomic/

Configure Local Maven Projects
===
For projects that depend on the "sean" repository

    <repositories>
       <repository>
           <id>sean</id>
           <name>sean</name>
           <url>http://thoughtfulcraftsmanship.com/nexus/content/repositories/sean/</url>
           <snapshots>
               <enabled>true</enabled>
           </snapshots>
       </repository>
    </repositories>

For projects that depend on onejar

    <pluginRepositories>
       <pluginRepository>
           <id>onejar</id>
           <url>http://onejar-maven-plugin.googlecode.com/svn/mavenrepo</url>
       </pluginRepository>
    </pluginRepositories>

For projects that depend on datomic

    <repositories>
       <repository>
           <id>datomic</id>
           <name>datomic</name>
           <url>http://thoughtfulcraftsmanship.com/nexus/content/repositories/datomic/</url>
       </repository>
    </repositories>

For projects that others can depend on

    <distributionManagement>
       <repository>
           <id>sean</id>
           <url>http://thoughtfulcraftsmanship.com:8081/nexus/content/repositories/sean/</url>
       </repository>
    </distributionManagement>
