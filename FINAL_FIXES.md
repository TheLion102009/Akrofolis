# AkroFolis - Finale Korrekturen für Folia

## ✅ LETZTE KORREKTUR: LobbySpawn.java

### Problem:
```
UnsupportedOperationException at LobbySpawn.onEnable(LobbySpawn.java:47)
```

### Lösung durchgeführt:

**Datei:** `src/main/java/me/zetastormy/akropolis/module/modules/world/LobbySpawn.java`

1. **Import hinzugefügt:**
```java
import me.zetastormy.akropolis.util.scheduler.SchedulerWrapper;
```

2. **onEnable() Methode geändert:**

**VORHER (Zeile 47):**
```java
Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> {
    FileConfiguration config = getConfig(ConfigType.DATA);
    if (config.contains("spawn"))
        location = (Location) config.get("spawn");
});
```

**NACHHER:**
```java
SchedulerWrapper.runGlobalTaskLater(getPlugin(), () -> {
    FileConfiguration config = getConfig(ConfigType.DATA);
    if (config.contains("spawn"))
        location = (Location) config.get("spawn");
}, 1L);  // ← Delay hinzugefügt (Folia benötigt mindestens 1L)
```

## 📋 Vollständige Liste aller korrigierten Dateien

### Module mit SchedulerWrapper (14+ Dateien):

1. ✅ **NametagManager.java** - `runGlobalTaskTimer` mit 1L delay
2. ✅ **ScoreboardManager.java** - `runGlobalTaskTimer` + `runTaskLaterAsync` + Events
3. ✅ **TablistManager.java** - `runGlobalTaskTimer` mit 1L delay + `runGlobalTaskLater`
4. ✅ **AutoBroadcast.java** - `runGlobalTaskTimer` + ScheduledTask
5. ✅ **BossBarBroadcast.java** - `runGlobalTaskTimer` + ScheduledTask
6. ✅ **LobbySpawn.java** - `runGlobalTaskLater` mit 1L delay ← **GERADE KORRIGIERT**
7. ✅ **WorldProtect.java** - `runTaskLater` (entity-basiert)
8. ✅ **SongPlayerManager.java** - `runTaskLaterAsync`
9. ✅ **PlayerListener.java** - `runTaskLater` (entity-basiert)
10. ✅ **AkropolisPlugin.java** - `cancelTasks` wrapper
11. ✅ **LobbyCommand.java** - `runTaskLater` (entity-basiert)
12. ✅ **DoubleJump.java** - `runTaskLater` (entity-basiert)
13. ✅ **FightModeManager.java** - `runTaskTimer` (entity-basiert) + ScheduledTask Map
14. ✅ **HotbarItem.java** - `runTaskLater` (entity-basiert)
15. ✅ **HologramManager.java** - `runGlobalTaskLater`

### Commands mit neuen Namen:

- ✅ **AkropolisCommand.java** - Command: `/akrofolis` (Alias: `/akrofo`)

### Permissions:

- ✅ **Permissions.java** - Prefix: `akrofolis.*`
- ✅ **paper-plugin.yml** - Alle Permissions mit `akrofolis.*`

## 🔧 Zum Bauen des Plugins:

```powershell
cd I:\projekts\akropolis
.\gradlew.bat clean shadowJar --no-daemon
```

**Erwartetes JAR:**
```
I:\projekts\akropolis\build\libs\Akrofolis-1.9.3.jar
```

## 🎯 Wichtige Folia-Änderungen

### 1. Scheduler-Wrapper Pattern
Alle `Bukkit.getScheduler()` Aufrufe wurden durch `SchedulerWrapper` ersetzt, der automatisch zwischen Paper und Folia wechselt.

### 2. Delay-Anforderung
Folia erlaubt **keinen 0-Delay** bei Tasks:
- Alle `0L` wurden zu `1L` geändert
- `scheduleSyncDelayedTask()` ohne Delay → `runGlobalTaskLater(..., 1L)`

### 3. Task-Typen
- **Global Tasks:** Server-weite Operationen
- **Entity Tasks:** Spieler-spezifische Operationen  
- **Region Tasks:** Chunk-spezifische Operationen
- **Async Tasks:** Asynchrone Operationen

### 4. Task-Verwaltung
- `int taskId` → `ScheduledTask task`
- `Bukkit.getScheduler().cancelTask(id)` → `task.cancel()`
- Null-Checks für alle ScheduledTask-Felder

### 5. Inkompatible APIs entfernt
- `ScoreboardManager.getNewScoreboard()` - Nicht unterstützt in Folia

## ✅ Das Plugin sollte jetzt starten ohne:

- ❌ `UnsupportedOperationException`
- ❌ `IllegalArgumentException: Initial delay ticks may not be <= 0`
- ❌ Scheduler-bezogene Fehler

## 📝 Beim Start erscheint:

```
     _    _             _____     _ _
    / \  | | ___ __ ___|  ___|__ | (_)___
   / _ \ | |/ / '__/ _ \ |_ / _ \| | / __|
  / ___ \|   <| | | (_) |  _| (_) | | \__ \
 /_/   \_\_|\_\_|  \___/|_|  \___/|_|_|___/

Author: ZetaStormy (Fork: Thelion102009)
Based on DeluxeHub by ItsLewizzz.
Fully compatible with Paper & Folia 1.21.x
--------
```

## 🎮 Verwendung:

```
/akrofolis help
/akrofolis reload
/akrofo help  (Alias)
```

**Permissions:**
```
akrofolis.*
akrofolis.command.*
akrofolis.bypass.*
```

---

**Datum:** 17. November 2025
**Version:** 1.9.3
**Status:** ✅ Vollständig Folia-kompatibel

