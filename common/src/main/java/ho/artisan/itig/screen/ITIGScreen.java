package ho.artisan.itig.screen;

import ho.artisan.itig.util.ItemInfoGetter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class ITIGScreen extends Screen {
    public static final Identifier TRANSLATION_SCREEN_BACKGROUND = new Identifier("itig:textures/translation_screen.png");

    //按钮的高和宽
    public static final int BUTTON_HEIGHT = 20;
    public static final int BUTTON_WIDTH = 95;

    //文本框及其提示文本
    public static TextFieldWidget TRANSLATION_EDIT_BOX;
    public static final Text TRANSLATION_EDIT_BOX_TEXT = Text.translatable("gui.igit.edit_box.hint");

    //按钮文本
    public static final Text OK_BUTTON_TEXT = Text.translatable("gui.igit.button.ok");
    public static final Text CANCEL_BUTTON_TEXT = Text.translatable("gui.igit.button.cancel");

    //其他的文本
    public static final Text TITLE_TEXT = Text.translatable("gui.igit.screen.title");
    public static final Text ORIGINAL_ARTICLE_TEXT = Text.translatable("gui.igit.screen.original_text");
    public static final Text LANG_KEY_TEXT = Text.translatable("gui.igit.screen.lang_key");
    public static final Text DISPLAY_NAME_TEXT = Text.translatable("gui.igit.screen.display_name");
    public static final Text PARENT_MOD_ID_TEXT = Text.translatable("gui.igit.screen.parent_mod_id");

    PlayerEntity player = MinecraftClient.getInstance().player;
    ItemStack item = player.getMainHandStack().getItem().getDefaultStack(); //玩家拿着的物品
    String modId = Registry.ITEM.getId(item.getItem()).getNamespace(); //物品的id
    String itemKey = item.getTranslationKey(); //物品的翻译键
    String sourceText = item.getName().getString(); //应该是英文原文，但是我现在想不出怎么实现（
    String itemDisplayName = item.getName().getString(); //物品目前所显示的名称

    public ITIGScreen() {
        super(Text.translatable(""));
    }

    @Override
    protected void init() {
        //确认按钮
        this.addDrawableChild(new ButtonWidget((this.width / 2 + 50), (this.height / 2) + 80, BUTTON_WIDTH, BUTTON_HEIGHT, OK_BUTTON_TEXT, button -> {
                    //不知道写什么，让Og干吧（
                }));
        //取消按钮
        this.addDrawableChild(new ButtonWidget((this.width - 100) / 2 - BUTTON_WIDTH, (this.height / 2) + 80, BUTTON_WIDTH, BUTTON_HEIGHT, CANCEL_BUTTON_TEXT, button -> this.client.setScreen(null)));

        //文本框及其里面提示
        TRANSLATION_EDIT_BOX = new TextFieldWidget(this.textRenderer, (this.width / 2) - 50, (this.height / 2) + 10, 120, 20, TRANSLATION_EDIT_BOX_TEXT) {
            {
                setSuggestion(TRANSLATION_EDIT_BOX_TEXT.getString());
            }
            @Override
            public void write(@NotNull String text) {
                super.write(text);
                if (getText().isEmpty())
                    setSuggestion(TRANSLATION_EDIT_BOX_TEXT.getString());
                else
                    setSuggestion(null);
            }

            @Override
            public void setCursor(int pos) {
                super.setCursor(pos);
                if (getText().isEmpty())
                    setSuggestion(TRANSLATION_EDIT_BOX_TEXT.getString());
                else
                    setSuggestion(null);
            }
        };
        this.addDrawable(TRANSLATION_EDIT_BOX);
        TRANSLATION_EDIT_BOX.setMaxLength(32767);
    }

    @Override
    public void tick() {
        super.tick();
        TRANSLATION_EDIT_BOX.tick();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            assert this.client != null;
            assert this.client.player != null;
            this.client.player.closeHandledScreen();
            return true;
        }
        if (TRANSLATION_EDIT_BOX.isFocused())
            return TRANSLATION_EDIT_BOX.keyPressed(key, b, c);
        return super.keyPressed(key, b, c);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.client.getTextureManager().bindTexture(TRANSLATION_SCREEN_BACKGROUND);

        int height = (this.height / 2);
        int TextColor = 0xFFFFFF;
        String ORIGINAL_TEXT = ORIGINAL_ARTICLE_TEXT.getString() + sourceText;
        String LANG_KEY = LANG_KEY_TEXT.getString() + itemKey;
        String DISPLAY_NAME = DISPLAY_NAME_TEXT.getString() + itemDisplayName;
        String PARENT_MODID = PARENT_MOD_ID_TEXT.getString() + modId;

        this.textRenderer.draw(matrixStack, TITLE_TEXT.getString(), this.width / (float) 2 - this.textRenderer.getWidth(TITLE_TEXT) / (float) 2, height - 105, -65536);
        this.textRenderer.draw(matrixStack, ORIGINAL_TEXT, this.width / (float) 2 - this.textRenderer.getWidth(ORIGINAL_TEXT) / (float) 2, height - 80, TextColor);
        this.textRenderer.draw(matrixStack, LANG_KEY, this.width / (float) 2 - this.textRenderer.getWidth(LANG_KEY) / (float) 2, height - 65, TextColor);
        this.textRenderer.draw(matrixStack, DISPLAY_NAME, this.width / (float) 2 - this.textRenderer.getWidth(DISPLAY_NAME) / (float) 2, height - 50, TextColor);
        this.textRenderer.draw(matrixStack, PARENT_MODID, this.width / (float) 2 - this.textRenderer.getWidth(PARENT_MODID) / (float) 2, height - 35, TextColor);

        TRANSLATION_EDIT_BOX.render(matrixStack, mouseX, mouseY, partialTicks);//渲染文字框

        drawCenteredText(matrixStack, this.textRenderer, this.title.getString(), this.width / 2, 8, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void removed() {
        super.removed(); //關閉此Gui
    }
}
