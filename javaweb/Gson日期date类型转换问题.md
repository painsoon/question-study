
**Gson 日期date类型转换问题**  

项目中遇到一个问题：日期在数据库中存的d是ate类型，后台把数据传到APP前端时用SimpleDateFormat格式化时不能正常显示24进制。

在app后台接口设计中，个人认为关于时间的数据应该用varchar类型，通过System.currentTimeMillis()获取保存到数据库。可以避免不同环境间或者Gson等工具封装时类型转换出现问题，而且在网络间传输也稳定。当然喽，数据库已经设计好了而且是正在运行的项目，该字段类型是不可能的了。  

Gson默认处理Date对象的序列化/反序列化是通过一个SimpleDateFormat对象来实现的，通过下面的代码去获取实例：DateFormat.getDateTimeInstance()在不同的locale环境中，这样获取到的SimpleDateFormat的模式字符串会不一样。

**解决**  

使用GsonBuilder来创建Gson对象，在创建过程中调用GsonBuilder.setDateFormat(String)指定一个固定的格式即可。例如：
Gson gson = new GsonBuilder()  
  .setDateFormat("yyyy-MM-dd HH:mm:ss")  
  .create();  
同样在APP端也要通过Gsonbuilder来创建对象才能正确接收。

**附：**
SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");     //12小时制  
SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     //24小时制

