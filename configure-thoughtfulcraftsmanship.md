Steps to configure cloud server for thoughtfulcraftsmanship.com
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
    useradd -m -d /home/nexus nexus
    passwd nexus
    useradd -m -d /home/git git
    passwd git

Update tools
===

    apt-get update
    apt-get autoremove
    apt-get upgrade
    apt-get install tree nginx supervisor git

Install Java 8
===

    wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u31-b13/jdk-8u31-linux-x64.tar.gz
    mkdir /opt/jdk
    tar -zxvf jdk-8u31-linux-x64.tar.gz -C /opt/jdk
    rm jdk-8u31-linux-x64.tar.gz
    update-alternatives --install /usr/bin/java java /opt/jdk/jdk1.8.0_31/bin/java 100
    java -version

Install Nexus
===

    wget --no-check-certificate http://download.sonatype.com/nexus/oss/nexus-latest-bundle.tar.gz
    tar -zxvf nexus-latest-bundle.tar.gz -C /usr/local/bin
    rm nexus-latest-bundle.tar.gz
    ln -s /usr/local/bin/nexus-2.11.2-03/bin/nexus /usr/local/bin/nexus
    chown -R nexus /usr/local/bin/nexus-2.11.2-03/ /usr/local/bin/sonatype-work/ /usr/local/bin/nexus

Configure nginx
===
Add the listed server section.
Don't forget to comment out the listed includes.
Serves nexus from 80 rather than 8081.
Make sure to set client_max_body_size to the biggest jar you want to upload.
By default it is 4G which just barely not enough for datomic.

    vim /etc/nginx/nginx.conf

    server {
        client_max_body_size 20M;
        listen       80;
        server_name  localhost;
        location / {
            return 301 nexus/;
        }
        location /nexus {
            proxy_pass http://127.0.0.1:8081;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
    }
    #include /etc/nginx/conf.d/*.conf;
    #include /etc/nginx/sites-enabled/*;

    nginx -s reload

Supervise nexus
===
Commands should be run as nexus rather than root, so set user to nexus.

    vim /etc/supervisor/conf.d/nexus.conf

    [program:nexus]
    command=nexus console
    user=nexus
    autostart=true
    autorestart=true
    stderr_logfile=/var/log/nexus.err.log
    stdout_logfile=/var/log/nexus.out.log

Launch supervisor
===

    supervisorctl reread
    supervisorctl update

Setup git on server
===

    useradd -m -d /home/git git
    passwd git
    su git
    mkdir .ssh
    chmod 700 .ssh
    touch .ssh/authorized_keys
    chmod 600 .ssh/authorized_keys
    vim .ssh/authorized_keys

add ssh keys

    mkdir repo.git
    cd repo.git
    git init --bare

Setup git on client

    git remote add origin git@thoughtfulcraftsmanship.com:/home/git/repo.git
    git push --set-upstream origin master
