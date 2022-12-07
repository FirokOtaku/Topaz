module firok.topaz {
	requires lombok;

	requires java.desktop;

	exports firok.topaz;
	exports firok.topaz.hash;
	exports firok.topaz.annotation;

	uses firok.topaz.IHashMapper;
	provides firok.topaz.IHashMapper
	with firok.topaz.hash.NoHashMapper,
			firok.topaz.hash.UUIDQuadrupleMapper,
			firok.topaz.hash.UUIDSextupleMapper;
}
