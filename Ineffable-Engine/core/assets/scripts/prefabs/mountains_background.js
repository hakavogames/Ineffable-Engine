AssetManager.loadAssetBundle(Gdx.files.internal("parallax/landscape/asset_bundle.json"));
AssetManager.loadAsset("sound",Gdx.files.internal("sounds/menu/ambient.wav"),"ambient");

var layers=new Array();
layers.push(new Sprite2D(AssetManager.getAsset("mountain-foreground-trees",Texture.class)));
layers.push(new Sprite2D(AssetManager.getAsset("mountain-trees",Texture.class)));
layers.push(new Sprite2D(AssetManager.getAsset("mountain-mountains",Texture.class)));
layers.push(new Sprite2D(AssetManager.getAsset("mountain-far",Texture.class)));
layers.push(new Sprite2D(AssetManager.getAsset("mountain-bg",Texture.class)));

var gameObject=new GameObject("background");
gameObject.addComponent(new Transform());
gameObject.addComponent(new SoundEffect(AssetManager.getAsset("ambient"),8.0));
gameObject.getComponent(SoundEffect.class).setLooping(true);
gameObject.getComponent(SoundEffect.class).sound.loop();

for(var i=0;i<layers.length;i++) {
	var amount=1/layers.length;
	var scroller=new ParallaxScroller(layers[i],Math.pow(amount*(i+1),0.125));
	scroller.layer=-i;
	gameObject.addComponent(scroller);
}

function getPrefab() {
    return new Prefab(gameObject);
}
