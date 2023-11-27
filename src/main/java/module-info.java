import firok.topaz.hash.IHashMapper;

/**
 * Topaz 主模块
 * */
module firok.topaz {
	requires lombok;
	requires org.jetbrains.annotations;
	requires java.desktop;
    requires java.compiler;

    exports firok.topaz;
	exports firok.topaz.hash;
	exports firok.topaz.annotation;
	exports firok.topaz.function;
	exports firok.topaz.design;
	exports firok.topaz.indev to firok.topaz.test;
	exports firok.topaz.resource;
	exports firok.topaz.general;
	exports firok.topaz.thread;
	exports firok.topaz.math;
	exports firok.topaz.reflection;
	exports firok.topaz.platform;
	exports firok.topaz.spring;

	uses IHashMapper;
	provides IHashMapper
	with firok.topaz.hash.NoHashMapper,
			firok.topaz.hash.UUIDQuadrupleMapper,
			firok.topaz.hash.UUIDSextupleMapper;
}
