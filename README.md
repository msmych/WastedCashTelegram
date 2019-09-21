# Wasted cash Telegram bot

Project [**help page**](https://telegra.ph/Wasted-cash-03-11)

### How to build
```
gradle clean shadowJar
```

### How to run

**Prod**
```
java -jar <jar> <telegram token> --prod --admin-id=<admin id>
```

**Test**
```
java -jar <jar> <telegram token> --test <api token> --admin-id=<admin id>
```

**Local**
```
java -jar <jar> <telegram token> --admin-id=<admin id>
```

**Optional parameters**
```
--whats-new
```
