### **Email**
When running the *.jar file, you must specify the `email.config.password` property in the environment variables to connect to the mailbox from which the emails will be sent in the application.
```sh
-Dmail.config.password=
```

If you want to disable or enable sending e-mails, you must add an additional parameter when running the * .jar file
```sh
-Dmail.config.enabled=
```
By default, sending e-mails is disabled

