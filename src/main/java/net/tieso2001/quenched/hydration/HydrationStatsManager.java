package net.tieso2001.quenched.hydration;

/* TODO
public class HydrationStatsManager extends JsonReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private Map<ResourceLocation, HydrationItem> hydrationItems = ImmutableMap.of();

    public HydrationStatsManager() {
        super(GSON, "quenched");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        Map<ResourceLocation, HydrationItem> map = Maps.newHashMap();
        objectIn.forEach((resourceLocation, jsonObject) -> {
            try {
                HydrationItem hydrationItem = read(jsonObject);
                if (hydrationItem == null) {
                    return;
                }
                map.put(resourceLocation, hydrationItem);
            } catch (IllegalArgumentException | JsonParseException ignored) {
            }
        });
        hydrationItems = map;
    }

    private HydrationItem read(JsonObject jsonObject) {
        String json = jsonObject.toString();
        return new Gson().fromJson(json, HydrationItem.class);
    }

    public Map<ResourceLocation, HydrationItem> getAllHydrationStats() {
        return this.hydrationItems;
    }
}*/
