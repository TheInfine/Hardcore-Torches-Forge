package com.github.wolfiewaffle.hardcore_torches.loot;

import com.github.wolfiewaffle.hardcore_torches.MainMod;
import com.github.wolfiewaffle.hardcore_torches.block.AbstractHardcoreTorchBlock;
import com.github.wolfiewaffle.hardcore_torches.config.Config;
import com.github.wolfiewaffle.hardcore_torches.util.ETorchState;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class TorchLootFunction extends LootItemConditionalFunction {

    public TorchLootFunction(LootItemCondition[] lootConditions) {
        super(lootConditions);
    }

    @Override
    public LootItemFunctionType getType() {
        return MainMod.HARDCORE_TORCH_LOOT_FUNCTION;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        BlockEntity blockEntity = context.getParam(LootContextParams.BLOCK_ENTITY);
        BlockState state = context.getParam(LootContextParams.BLOCK_STATE);
        ItemStack itemStack = new ItemStack(state.getBlock().asItem());
        ETorchState torchState;
        ETorchState dropTorchState;

        // Non-fuel modifications
        if (state.getBlock() instanceof AbstractHardcoreTorchBlock) {
            torchState = ((AbstractHardcoreTorchBlock) state.getBlock()).burnState;
            dropTorchState = torchState;

            // If torches burn out when dropped
            if (Config.torchesBurnWhenDropped.get()) {
                if (dropTorchState != ETorchState.BURNT && dropTorchState != ETorchState.UNLIT) {
                    dropTorchState = ETorchState.BURNT;
                }
            } else {
                // If torches extinguish when dropped
                if (Config.torchesExtinguishWhenBroken.get()) {
                    if (dropTorchState != ETorchState.BURNT) {
                        dropTorchState = ETorchState.UNLIT;
                    }
                }
            }

            // If smoldering, drop unlit
            if (dropTorchState == ETorchState.SMOLDERING) {
                dropTorchState = ETorchState.UNLIT;
            }

            // Set item stack
            if (Config.burntStick.get() && dropTorchState == ETorchState.BURNT) {
                // If burnt torches drop sticks
                itemStack = new ItemStack(Items.STICK);
            } else {
                itemStack = getChangedStack(state, dropTorchState);
            }
        }

        return itemStack;
    }

    private ItemStack getChangedStack(BlockState state, ETorchState torchState) {
        return new ItemStack(((AbstractHardcoreTorchBlock) state.getBlock()).group.getStandingTorch(torchState).asItem());
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<TorchLootFunction> {

        public TorchLootFunction deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext, LootItemCondition[] lootConditions) {
            return new TorchLootFunction(lootConditions);
        }
    }
}
