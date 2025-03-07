package team.creative.creativecore.common.gui.controls.simple;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.creativecore.client.render.text.CompiledText;
import team.creative.creativecore.common.gui.GuiChildControl;
import team.creative.creativecore.common.gui.event.GuiControlChangedEvent;
import team.creative.creativecore.common.gui.style.ControlFormatting;
import team.creative.creativecore.common.util.math.geo.Rect;
import team.creative.creativecore.common.util.text.ITextCollection;
import team.creative.creativecore.common.util.text.TextListBuilder;

public class GuiStateButton extends GuiButton {
    
    private int index = 0;
    public CompiledText[] states;
    public boolean autosize;
    
    public GuiStateButton(String name, ITextCollection states) {
        this(name, 0, states);
    }
    
    public GuiStateButton(String name, int index, ITextCollection states) {
        super(name, null);
        this.pressed = button -> {
            if (button == 1)
                previousState();
            else
                nextState();
        };
        this.index = index;
        this.autosize = true;
        buildStates(states);
    }
    
    public GuiStateButton(String name, int index, String... states) {
        this(name, index, new TextListBuilder().add(states));
    }
    
    protected void buildStates(ITextCollection builder) {
        states = builder.build();
        if (index >= states.length)
            index = 0;
    }
    
    @Override
    public void flowX(int width, int preferred) {
        for (CompiledText text : states)
            text.setDimension(width, Integer.MAX_VALUE);
    }
    
    @Override
    public void flowY(int height, int preferred) {
        for (CompiledText text : states)
            text.setMaxHeight(height);
    }
    
    @Override
    public int preferredWidth() {
        int width = 0;
        for (CompiledText text : states)
            width = Math.max(width, text.getTotalWidth());
        return width;
    }
    
    @Override
    public int preferredHeight() {
        int height = 0;
        for (CompiledText text : states)
            height = Math.max(height, text.getTotalHeight());
        return height;
    }
    
    public void setState(int index) {
        this.index = index;
    }
    
    public int getState() {
        return index;
    }
    
    public void previousState() {
        int state = getState();
        state--;
        if (state < 0)
            state = states.length - 1;
        if (state >= states.length)
            state = 0;
        setState(state);
        raiseEvent(new GuiControlChangedEvent(this));
    }
    
    public void nextState() {
        int state = getState();
        state++;
        if (state < 0)
            state = states.length - 1;
        if (state >= states.length)
            state = 0;
        setState(state);
        raiseEvent(new GuiControlChangedEvent(this));
    }
    
    @Override
    @OnlyIn(value = Dist.CLIENT)
    protected void renderContent(PoseStack matrix, GuiChildControl control, Rect rect, int mouseX, int mouseY) {
        CompiledText text = states[index];
        matrix.translate(rect.getWidth() / 2 - text.usedWidth / 2, rect.getHeight() / 2 - text.usedHeight / 2, 0);
        text.render(matrix);
    }
    
    @Override
    public void closed() {}
    
    @Override
    public void tick() {}
    
    @Override
    public ControlFormatting getControlFormatting() {
        return ControlFormatting.CLICKABLE;
    }
    
}
