package com.minecolonies.coremod.entity.ai.mobs;

import com.minecolonies.coremod.sounds.BarbarianSounds;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Asherslab on 5/6/17.
 */
public class EntityArcherBarbarian extends AbstractArcherBarbarian {

    public EntityArcherBarbarian(World worldIn)
    {
        super(worldIn);
    }

    SoundEvent getStepSound()
    {
        return SoundEvents.ENTITY_ZOMBIE_STEP;
    }

    protected EntityArrow getArrow(float p_190726_1_)
    {
        ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

        if (itemstack.getItem() == Items.SPECTRAL_ARROW)
        {
            EntitySpectralArrow entityspectralarrow = new EntitySpectralArrow(this.world, this);
            entityspectralarrow.setEnchantmentEffectsFromEntity(this, p_190726_1_);
            return entityspectralarrow;
        }
        else
        {
            EntityArrow entityarrow = super.getArrow(p_190726_1_);

            if (itemstack.getItem() == Items.TIPPED_ARROW && entityarrow instanceof EntityTippedArrow)
            {
                ((EntityTippedArrow)entityarrow).setPotionEffect(itemstack);
            }

            return entityarrow;
        }
    }

    @Override
    public int getMaxSpawnedInChunk()
    {
        return 5;
    }

    @Override
    protected boolean canDespawn()
    {
        return (world.isDaytime());
    }

    @Override
    protected SoundEvent getHurtSound() {
        return BarbarianSounds.barbarianHurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BarbarianSounds.barbarianDeath;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return BarbarianSounds.barbarianSay;
    }
}