package com.itheima.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 代码生成器演示
 * </p>
 */
public class MpGenerator {

    private static String author = "zzj";
    private static String filepath = new File("").getAbsolutePath();

    private static String[] tables = new String[]{
            "b_gos_zhiya"
//            "scrm_crontab","scrm_device","scrm_friends_shopno","scrm_log","scrm_speech",
            };//table名字


    public static void main(String[] args){
//        assert (false) : "代码生成属于危险操作，请确定配置后取消断言执行代码生成！";
        AutoGenerator mpg = new AutoGenerator();
        // 选择 freemarker 引擎，默认 Velocity
        //mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(new TableFill("ASDD_SS", FieldFill.INSERT_UPDATE));
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setAuthor(author); //作者
        gc.setOutputDir(filepath+"/src/main/java");
        gc.setFileOverride(true);// 是否覆盖同名文件，默认是false
        gc.setActiveRecord(true);// 不需要ActiveRecord特性的请改为false
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setOpen(false);//生成后打开文件夹
        /* 自定义文件命名，注意 %s 会自动填充表实体属性！ */
        gc.setEntityName("%sEntity");
        gc.setMapperName("%sDao");
        gc.setXmlName("%sDao");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert(){
            //TODO 这里做自定义类型转换
        });
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("admin");
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/test?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf8&useSSL=false");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
//        strategy.setTablePrefix(prefixs );// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(tables); // 需要生成的表
        strategy.setTableFillList(tableFillList);
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
        // strategy.setSuperEntityClass("com.baomidou.demo.TestEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 server 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 server 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.baomidou.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuilderModel(true);
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.itheima");
        pc.setController("controller");// 这里是控制器包名，默认 web
        pc.setEntity("data.pojo");
        pc.setMapper("data.mapper");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
//         pc.setModuleName("test");
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
         InjectionConfig cfg = new InjectionConfig() {
         @Override
         public void initMap() {
         Map<String, Object> map = new HashMap<String, Object>();
         map.put("abc", this.getConfig().getGlobalConfig().getAuthor() +
         "-mp");
         this.setMap(map);
         }
         };

        // // 自定义 xxList.jsp 生成
         List<FileOutConfig> focList = new ArrayList<>();
        // focList.add(new FileOutConfig("/template/list.jsp.vm") {
        // @Override
        // public String outputFile(TableInfo tableInfo) {
        // // 自定义输入文件名称
        // return "D://my_" + tableInfo.getEntityName() + ".jsp";
        // }
        // });
        // cfg.setFileOutConfigList(focList);
        // mpg.setCfg(cfg);
        //
         // 调整 xml 生成目录演示
         focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
         @Override
         public String outputFile(TableInfo tableInfo) {
         return filepath+"/src/main/resources/mapper/" + tableInfo.getEntityName().replace("Entity","") + "Mapper.xml";
         }
         });
         cfg.setFileOutConfigList(focList);
         mpg.setCfg(cfg);
        //
        // // 关闭默认 xml 生成，调整生成 至 根目录
         TemplateConfig tc = new TemplateConfig();
         tc.setXml(null);
         tc.setController(null);//不生成controller
//         tc.setService(null);
//         tc.setServiceImpl(null);
//         tc.setEntity(null);
//         tc.setMapper(null);
         mpg.setTemplate(tc);

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
        // TemplateConfig tc = new TemplateConfig();
        // tc.setController("...");
        // tc.setEntity("...");
        // tc.setMapper("...");
        // tc.setXml("...");
        // tc.setService("...");
        // tc.setServiceImpl("...");
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        // mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();

        // 打印注入设置【可无】
        // System.err.println(mpg.getCfg().getMap().get("abc"));
    }
}

