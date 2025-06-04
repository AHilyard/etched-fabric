package gg.moonflower.etched.common.recipe;

import gg.moonflower.etched.core.registry.EtchedItems;
import gg.moonflower.etched.core.registry.EtchedRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MusicDiscCloningRecipe extends CustomRecipe {

    public MusicDiscCloningRecipe(ResourceLocation resourceLocation, CraftingBookCategory category) {
        super(resourceLocation, category);
    }

    @Override
    public boolean matches(CraftingContainer container, @NotNull Level level) {
        ItemStack base = ItemStack.EMPTY;
        ItemStack copy = ItemStack.EMPTY;

        for (int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack stack = container.getItem(j);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.is(ItemTags.MUSIC_DISCS)) {
                if (!base.isEmpty()) {
                    return false;
                }

                base = stack;
            } else {
                if (!copy.isEmpty() || !stack.is(EtchedItems.BLANK_MUSIC_DISC.get())) {
                    return false;
                }

                copy = stack;
            }
        }

        return !base.isEmpty() && !copy.isEmpty();
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container, @NotNull RegistryAccess registryAccess) {
        ItemStack base = ItemStack.EMPTY;
        ItemStack copy = ItemStack.EMPTY;

        for (int j = 0; j < container.getContainerSize(); ++j) {
            ItemStack stack = container.getItem(j);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.is(ItemTags.MUSIC_DISCS)) {
                if (!base.isEmpty()) {
                    return ItemStack.EMPTY;
                }

                base = stack;
            } else {
                if (!copy.isEmpty() || !stack.is(EtchedItems.BLANK_MUSIC_DISC.get())) {
                    return ItemStack.EMPTY;
                }

                copy = stack;
            }
        }

        if (base.isEmpty() || copy.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return base.copyWithCount(1);
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> list = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < list.size(); ++i) {
            ItemStack stack = container.getItem(i);
            if (stack.getItem() == EtchedItems.BLANK_MUSIC_DISC.get()) {
                list.set(i, new ItemStack(Items.AIR));
            } else if (stack.is(ItemTags.MUSIC_DISCS)) {
                list.set(i, stack.copyWithCount(1));
            }
        }

        return list;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EtchedRecipes.CLONE_MUSIC_DISC;
    }
}
