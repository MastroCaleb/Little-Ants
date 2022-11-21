package toxican.caleb.ants.features.bushes.nest_decorator;

import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import toxican.caleb.ants.mixin.TreeDecoratorTypeInvoker;

//These are the bushes that spawn

public class AntsDecorations {

    public static TreeDecoratorType<RedNestTreeDecorator> RED_NEST_DECORATOR;
    public static TreeDecoratorType<BrownNestTreeDecorator> BROWN_NEST_DECORATOR;
    public static TreeDecoratorType<BlackNestTreeDecorator> BLACK_NEST_DECORATOR;
    public static TreeDecoratorType<MuddyNestTreeDecorator> MUDDY_NEST_DECORATOR;

    public static void init()
    {
        RED_NEST_DECORATOR = TreeDecoratorTypeInvoker.callRegister("red_nest_decorator", RedNestTreeDecorator.CODEC);
        BROWN_NEST_DECORATOR = TreeDecoratorTypeInvoker.callRegister("brown_nest_decorator", BrownNestTreeDecorator.CODEC);
        BLACK_NEST_DECORATOR = TreeDecoratorTypeInvoker.callRegister("black_nest_decorator", BlackNestTreeDecorator.CODEC);
        MUDDY_NEST_DECORATOR = TreeDecoratorTypeInvoker.callRegister("muddy_nest_decorator", MuddyNestTreeDecorator.CODEC);
    }
}
