# docker-mail-server-mock

AWS SES mocked server and normal SMTP mocked server for test purpose.

## 🔧 Set up

### **Install components.**

* [Docker](https://docs.docker.com/get-docker/)
* [Java](https://openjdk.java.net/)
* [Maven](https://maven.apache.org/install.html)
* [AWS cli](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)

### **Setup AWS credentials.**

* [AWS credentials](https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html)

## 🚀 Quick start

### **Start Project.**

#### To start, execute this:
```shell
docker-compose up -d --no-deps --build
```

#### For no cache, execute this:
```shell
docker-compose build --no-cache
docker-compose up -d --force-recreate
```

### **Local test.**

#### Compile java project
```shell
mvn clean install
```

#### Compile at full speed, it won't make any difference since it's a small source code
```shell
mvn clean install -T 1.5C
```

## 💻 Run it

### **AWS SES test.**

``` shell
mvn exec:java -Dexec.mainClass="mail.SESAWSSample"
```

### **SMTP test.**

``` shell
mvn exec:java -Dexec.mainClass="mail.SMTPAWSSample"
```

## 🧹 Clean up

### **Check docker and images.**
```shell
docker ps -a
docekr images -a
```

### **Kill docker and rm image.**
```shell
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)
```

### **Remove dangling.**
```shell
docker rmi $(docker images -q -f dangling=true)
```

### **Remove all images.**
```shell
docker image prune -a
```

## 📖 Conclusion

After testing it with both javax.mail and aws-java-sdk. It seemed that javax.mail requires a connection first then perform send and receive mail processes.
However, this can't connect to aws-ses-local as it tries infinitely connecting it, never achieving to the established state.
I was able to connect to aws-ses using ```props.put("mail.transport.protocol", "aws");``` instead of `smtp`.


Therefore, I conclude that Amazon SES can be connected via javax.mail (SMTP) only if there is a [SMTP credential](https://docs.aws.amazon.com/ses/latest/DeveloperGuide/smtp-credentials.html) from AWS console.
Otherwise, we can use transport.protocol aws or aws-java-sdk (Amazon SES sdk) with the AWS credential.


