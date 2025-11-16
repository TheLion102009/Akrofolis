/*
 * This file is part of Akrofolis
 *
 * Copyright (c) 2025 DevBlook Team and others
 *
 * Akrofolis free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Akropolis is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Akropolis. If not, see <http://www.gnu.org/licenses/>.
 */

package me.zetastormy.akropolis.util.scheduler;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

/**
 * Abstraction layer for scheduling tasks that works on both Paper and Folia.
 * Automatically detects the server type and uses the appropriate scheduler.
 */
public class SchedulerWrapper {
    private static final boolean IS_FOLIA;

    static {
        boolean folia;
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.AsyncScheduler");
            folia = true;
        } catch (ClassNotFoundException e) {
            folia = false;
        }
        IS_FOLIA = folia;
    }

    /**
     * Check if the server is running Folia.
     */
    public static boolean isFolia() {
        return IS_FOLIA;
    }

    /**
     * Run a task for an entity. On Folia, this uses the entity's scheduler.
     * On Paper, this uses the global scheduler.
     */
    public static void runTask(Plugin plugin, Entity entity, Runnable task) {
        if (IS_FOLIA) {
            entity.getScheduler().run(plugin, scheduledTask -> task.run(), null);
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    /**
     * Run a delayed task for an entity.
     */
    public static void runTaskLater(Plugin plugin, Entity entity, Runnable task, long delay) {
        if (IS_FOLIA) {
            entity.getScheduler().runDelayed(plugin, scheduledTask -> task.run(), null, delay);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delay);
        }
    }

    /**
     * Run a repeating task for an entity.
     */
    public static ScheduledTask runTaskTimer(Plugin plugin, Entity entity, Runnable task, long delay, long period) {
        if (IS_FOLIA) {
            return entity.getScheduler().runAtFixedRate(plugin, scheduledTask -> task.run(), null, delay, period);
        } else {
            int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delay, period);
            return new BukkitTaskWrapper(taskId);
        }
    }

    /**
     * Run a task at a specific location. On Folia, this uses the region scheduler.
     * On Paper, this uses the global scheduler.
     */
    public static void runTask(Plugin plugin, Location location, Runnable task) {
        if (IS_FOLIA) {
            Bukkit.getRegionScheduler().run(plugin, location, scheduledTask -> task.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    /**
     * Run a delayed task at a specific location.
     */
    public static void runTaskLater(Plugin plugin, Location location, Runnable task, long delay) {
        if (IS_FOLIA) {
            Bukkit.getRegionScheduler().runDelayed(plugin, location, scheduledTask -> task.run(), delay);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delay);
        }
    }

    /**
     * Run a global synchronous task. On Folia, this uses the global region scheduler.
     * On Paper, this uses the global scheduler.
     */
    public static void runGlobalTask(Plugin plugin, Runnable task) {
        if (IS_FOLIA) {
            Bukkit.getGlobalRegionScheduler().run(plugin, scheduledTask -> task.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    /**
     * Run a delayed global synchronous task.
     */
    public static void runGlobalTaskLater(Plugin plugin, Runnable task, long delay) {
        if (IS_FOLIA) {
            Bukkit.getGlobalRegionScheduler().runDelayed(plugin, scheduledTask -> task.run(), delay);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, delay);
        }
    }

    /**
     * Run a repeating global synchronous task.
     */
    public static ScheduledTask runGlobalTaskTimer(Plugin plugin, Runnable task, long delay, long period) {
        if (IS_FOLIA) {
            return Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTask -> task.run(), delay, period);
        } else {
            int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, task, delay, period);
            return new BukkitTaskWrapper(taskId);
        }
    }

    /**
     * Run an asynchronous task.
     */
    public static void runTaskAsync(Plugin plugin, Runnable task) {
        if (IS_FOLIA) {
            Bukkit.getAsyncScheduler().runNow(plugin, scheduledTask -> task.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        }
    }

    /**
     * Run a delayed asynchronous task.
     */
    public static void runTaskLaterAsync(Plugin plugin, Runnable task, long delay) {
        if (IS_FOLIA) {
            Bukkit.getAsyncScheduler().runDelayed(plugin, scheduledTask -> task.run(), delay * 50, TimeUnit.MILLISECONDS);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
        }
    }

    /**
     * Run a repeating asynchronous task.
     */
    public static ScheduledTask runTaskTimerAsync(Plugin plugin, Runnable task, long delay, long period) {
        if (IS_FOLIA) {
            return Bukkit.getAsyncScheduler().runAtFixedRate(plugin, scheduledTask -> task.run(),
                delay * 50, period * 50, TimeUnit.MILLISECONDS);
        } else {
            int taskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, task, delay, period);
            return new BukkitTaskWrapper(taskId);
        }
    }

    /**
     * Cancel all tasks for a plugin.
     */
    public static void cancelTasks(Plugin plugin) {
        if (IS_FOLIA) {
            Bukkit.getGlobalRegionScheduler().cancelTasks(plugin);
            Bukkit.getAsyncScheduler().cancelTasks(plugin);
        } else {
            Bukkit.getScheduler().cancelTasks(plugin);
        }
    }

    /**
     * Wrapper for Bukkit task IDs to provide ScheduledTask interface.
     */
    private static class BukkitTaskWrapper implements ScheduledTask {
        private final int taskId;

        BukkitTaskWrapper(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public Plugin getOwningPlugin() {
            return Bukkit.getScheduler().getPendingTasks().stream()
                .filter(task -> task.getTaskId() == taskId)
                .findFirst()
                .map(org.bukkit.scheduler.BukkitTask::getOwner)
                .orElse(null);
        }

        @Override
        public boolean isRepeatingTask() {
            return true;
        }

        @Override
        public CancelledState cancel() {
            Bukkit.getScheduler().cancelTask(taskId);
            return CancelledState.CANCELLED_BY_CALLER;
        }

        @Override
        public boolean isCancelled() {
            return Bukkit.getScheduler().getPendingTasks().stream()
                .noneMatch(task -> task.getTaskId() == taskId);
        }

        @Override
        public ExecutionState getExecutionState() {
            return isCancelled() ? ExecutionState.CANCELLED : ExecutionState.RUNNING;
        }
    }
}

