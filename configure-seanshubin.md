Steps to configure cloud server for seanshubin.com
===

Connecting
===
I use digital ocean "droplets", created with ssh keys.
Choose Ubuntu 14.10 x64.
When a droplet is recreated, the host key changes, so to connect you will also remove it from known hosts.

    ssh-keygen -R 123.456.789.012

when using ssh, use this format: root@123.456.789.012

Add a user
===
    useradd -m -d /home/sean sean
    passwd sean

Update tools
===

    apt-get update
    apt-get autoremove
    apt-get upgrade
    apt-get install tree nginx supervisor inotify-tools

Configure nginx
===
Add the listed server section.
Don't forget to comment out the listed includes.

    vim /etc/nginx/nginx.conf

    http {
    server {
        listen       80;
        server_name  localhost;
        root /home/sean/Copy/www;
        error_page 404 /index.html;
        location /foo/ {
            proxy_pass http://127.0.0.1:4000;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
        location /bar/ {
            proxy_pass http://127.0.0.1:5000;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
        location / {}
    }
    #include /etc/nginx/conf.d/*.conf;
    #include /etc/nginx/sites-enabled/*;
    }

    nginx -s reload

Install copy
===

    wget https://copy.com/install/linux/Copy.tgz
    tar xvf Copy.tgz -C /usr/local/bin
    rm Copy.tgz

Fix copy permissions
===
Copy sets permissions to 700.
I need them to be 775 for directories, and 664 for files.
So I have written this script to update the permissions

    vim /usr/local/bin/update-permissions

    #!/bin/bash
    find /home/sean/Copy/ -mindepth 1 -type d ! -iname ".*" -exec chmod 775 {} \;
    find /home/sean/Copy/ -type f ! -iname ".*" -exec chmod 664 {} \;
    find /home/sean/Copy/ -type f -iname "*.jar" -exec chmod 775 {} \;

    chmod +x /usr/local/bin/update-permissions

Monitor copy permissions
===
Change permissions of files synced by copy as they come in

    vim /usr/local/bin/monitor-permissions

    #!/bin/bash
    inotifywait --monitor --recursive --event moved_to --exclude "^\." /home/sean/Copy/ | while read FILE
    do
        BASE_DIR=$(echo $FILE | awk '{print $1}')
        EVENT_INFO=$(echo $FILE | awk '{print $2}')
        FILE_NAME=$(echo $FILE | awk '{print $3}')
        if [[ $EVENT_INFO = MOVED_TO,ISDIR || ($EVENT_INFO = MOVED_TO && $FILE_NAME =~ \.jar$) ]]
        then
            echo "chmod 775 $BASE_DIR$FILE_NAME"
            chmod 775 $BASE_DIR$FILE_NAME
        else
            echo "chmod 664 $BASE_DIR$FILE_NAME"
            chmod 664 $BASE_DIR$FILE_NAME
        fi
    done

    chmod +x /usr/local/bin/monitor-permissions

Supervise permission monitoring
===
Indicate commands should be run as sean rather than root by setting user to sean.
The script update-www-permissions is expected to exit quickly, so indicate that by setting startsecs to 0.

    vim /etc/supervisor/conf.d/permissions.conf

    [program:monitor-permissions]
    command=/usr/local/bin/monitor-permissions
    user=sean
    autostart=true
    autorestart=true
    stderr_logfile=/var/log/monitor-permissions.err.log
    stdout_logfile=/var/log/monitor-permissions.out.log

    [program:update-permissions]
    command=/usr/local/bin/update-permissions
    user=sean
    autostart=true
    autorestart=false
    startsecs=0
    stderr_logfile=/var/log/update-permissions.err.log
    stdout_logfile=/var/log/update-permissions.out.log

Supervise copy
===

    vim /etc/supervisor/conf.d/copy.conf

    [program:copy]
    command=/usr/local/bin/copy/x86_64/CopyConsole -username=******** -password=******** -root=/home/sean/Copy
    user=sean
    autostart=true
    autorestart=true
    stderr_logfile=/var/log/copy.err.log
    stdout_logfile=/var/log/copy.out.log

Install Java 8
===

    wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u31-b13/jdk-8u31-linux-x64.tar.gz
    mkdir /opt/jdk
    tar -zxvf jdk-8u31-linux-x64.tar.gz -C /opt/jdk
    rm jdk-8u31-linux-x64.tar.gz
    update-alternatives --install /usr/bin/java java /opt/jdk/jdk1.8.0_31/bin/java 100
    java -version

Supervise Applications
===

    vim /etc/supervisor/conf.d/apps.conf

    [program:foo]
    command=java -jar /home/sean/Copy/apps/foo.jar 4000
    user=sean
    autostart=true
    autorestart=true
    stderr_logfile=/var/log/foo.err.log
    stdout_logfile=/var/log/foo.out.log

    [program:bar]
    command=java -jar /home/sean/Copy/apps/bar.jar 5000
    user=sean
    autostart=true
    autorestart=true
    stderr_logfile=/var/log/bar.err.log
    stdout_logfile=/var/log/bar.out.log

Launch supervisor
===

    supervisorctl reread
    supervisorctl update
