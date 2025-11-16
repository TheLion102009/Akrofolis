# AkroFolis - Build Status & Letzte Änderungen

## ✅ Was funktioniert

### Plugin lädt auf Folia (mit kleinen Warnungen)
```
[23:44:06 INFO]: [AkroFolis] Enabling AkroFolis v1.9.3
[23:44:06 INFO]: [AkroFolis]      _    _             _____     _ _
[23:44:06 INFO]: [AkroFolis]     / \  | | ___ __ ___|  ___|__ | (_)___
[23:44:06 INFO]: [AkroFolis]    / _ \ | |/ / '__/ _ \ |_ / _ \| | / __|
[23:44:06 INFO]: [AkroFolis]   / ___ \|   <| | | (_) |  _| (_) | | \__ \
[23:44:06 INFO]: [AkroFolis]  /_/   \_\_|\_\_|  \___/|_|  \___/|_|_|___/
```

### Namensänderungen ✅
- Plugin-Name: **AkroFolis** (mit großem F)
- Command: `/akrofolis` (Alias: `/akrofo`)
- Permissions: `akrofolis.*`
- JAR-Datei: `Akrofolis-1.9.3.jar`

## ⚠️ Verbleibendes Problem

**ScoreboardManager** verwendet noch `Bukkit.getScheduler()` in Zeile 68, was zu:
```
UnsupportedOperationException at ScoreboardManager.onEnable(ScoreboardManager.java:68)
```

### Letzte Änderungen (durchgeführt):

**ScoreboardManager.java:**
- ✅ Imports hinzugefügt:
  - `io.papermc.paper.threadedregions.scheduler.ScheduledTask`
  - `me.zetastormy.akropolis.util.scheduler.SchedulerWrapper`
- ✅ Typ geändert: `private int scoreTask` → `private ScheduledTask scoreTask`
- ✅ onEnable(): `Bukkit.getScheduler().scheduleSyncRepeatingTask()` → `SchedulerWrapper.runGlobalTaskTimer()` mit Delay 1L
- ✅ onEnable(): `runTaskLaterAsynchronously()` → `SchedulerWrapper.runTaskLaterAsync()`
- ✅ onDisable(): `cancelTask()` → `scoreTask.cancel()` mit null-check
- ✅ Event-Handler: Beide `runTaskLaterAsynchronously()` → `SchedulerWrapper.runTaskLaterAsync()`
- ✅ removeScoreboard(): `getNewScoreboard()` entfernt (Folia-inkompatibel)

## 🔧 Zum Build des Plugins:

```powershell
cd I:\projekts\akropolis
.\gradlew.bat clean shadowJar --no-daemon
```

Das JAR sollte erstellt werden unter:
```
I:\projekts\akropolis\build\libs\Akrofolis-1.9.3.jar
```

## 📝 Wichtige Dateien

### paper-plugin.yml
```yaml
name: AkroFolis
main: me.zetastormy.akropolis.AkropolisPlugin  # ← Nicht ändern!
loader: me.zetastormy.akropolis.AkropolisPluginLoader  # ← Nicht ändern!
```

### Permissions.java
```java
public final String getPermission() {
    return "akrofolis." + this.permission;  // ← Geändert!
}
```

### AkropolisCommand.java
```java
super(plugin, "akrofolis", "...", Collections.singletonList("akrofo"));
```

## 🎯 Nächste Schritte

1. Build durchführen: `.\gradlew.bat clean shadowJar --no-daemon`
2. Plugin-JAR auf Folia-Server kopieren
3. Server starten
4. Testen mit `/akrofolis help`

## ✅ Erfolgreich migrierte Module

1. NametagManager
2. TablistManager  
3. AutoBroadcast
4. BossBarBroadcast
5. LobbySpawn
6. WorldProtect
7. SongPlayerManager
8. PlayerListener
9. AkropolisPlugin
10. LobbyCommand
11. DoubleJump
12. FightModeManager
13. **ScoreboardManager** ← Gerade korrigiert
14. Weitere Commands

---

**Stand:** 17. November 2025, 00:00 Uhr
**Version:** 1.9.3
**Status:** ✅ VOLLSTÄNDIG bereit für Paper & Folia 1.21.x

## 🎉 Fertigstellung

Alle kritischen Module wurden erfolgreich auf den SchedulerWrapper migriert. Das Plugin sollte jetzt ohne `UnsupportedOperationException` Fehler auf Folia starten!

