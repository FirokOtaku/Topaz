import firok.topaz.hash.IHashMapper;

/**
 * Topaz 主模块
 * */
module firok.topaz {
	requires lombok;
	requires org.jetbrains.annotations;
	requires static java.desktop;
    requires java.compiler;
    requires static redis.clients.jedis;
    requires static io.ebean.api;
	requires static org.apache.commons.pool2;

    exports firok.topaz;
	exports firok.topaz.hash;
	exports firok.topaz.annotation;
	exports firok.topaz.function;
	exports firok.topaz.design;
    exports firok.topaz.resource;
	exports firok.topaz.general;
	exports firok.topaz.general.converts;
	exports firok.topaz.thread;
	exports firok.topaz.math;
	exports firok.topaz.reflection;
	exports firok.topaz.platform;
	exports firok.topaz.spring;
//    exports firok.topaz.indev;
	exports firok.topaz.database;
	exports firok.topaz.integration.redis;
    exports firok.topaz.integration.ebean;

	//	for test only
	opens firok.topaz.resource to firok.topaz.test;
    opens firok.topaz.indev to firok.topaz.test;

    uses IHashMapper;
	provides IHashMapper
	with firok.topaz.hash.NoHashMapper,
			firok.topaz.hash.UUIDQuadrupleMapper,
			firok.topaz.hash.UUIDSextupleMapper;
}
