import firok.topaz.hash.IHashMapper;

module firok.topaz {
	requires lombok;
	requires org.jetbrains.annotations;
	requires java.desktop;

	exports firok.topaz;
	exports firok.topaz.hash;
	exports firok.topaz.annotation;
	exports firok.topaz.function;
	exports firok.topaz.design;
	exports firok.topaz.indev;
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
