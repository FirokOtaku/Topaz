open module firok.topaz {
	requires lombok;

	requires transitive org.graalvm.sdk;

	requires java.desktop;

	exports firok.topaz;
	exports firok.topaz.hash;

	uses firok.topaz.IHashMapper;
	provides firok.topaz.IHashMapper
	with firok.topaz.hash.NoHashMapper,
			firok.topaz.hash.UUIDQuadrupleMapper,
			firok.topaz.hash.UUIDSextupleMapper;
}
