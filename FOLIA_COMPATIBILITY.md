# Folia-Kompatibilität für Akrofolis

## ✅ Was wurde implementiert

### 1. Kern-Infrastruktur (Fertig)
- **SchedulerWrapper.java** - Vollständige Abstraktionsschicht für Scheduler
  - Automatische Erkennung von Paper vs. Folia
  - Entity-basierte Scheduler (`runTask`, `runTaskLater`, `runTaskTimer`)
  - Location-basierte Scheduler (Region Scheduler)
  - Globale Scheduler (GlobalRegionScheduler)
  - Asynchrone Scheduler (AsyncScheduler)
  - Abwärtskompatibilität mit Paper

- **AkrofolisPluginBootstrap.java** - Bootstrap-Klasse für Folia
  - Optional: Kann aktiviert werden durch Hinzufügen von `bootstrapper: me.zetastormy.Akrofolis.AkrofolisPluginBootstrap` in paper-plugin.yml

- **paper-plugin.yml** - Aktualisiert mit `folia-supported: true`

### 2. Was noch zu tun ist

Da durch ein technisches Problem beim automatischen Update die Änderungen verloren gingen, müssen folgende Dateien manuell aktualisiert werden:

#### Benötigte Änderungen pro Datei:

**Für ALLE folgenden Dateien:**

1. Import hinzufügen (nach den anderen me.zetastormy.Akrofolis.util Imports):
```java
import me.zetastormy.Akrofolis.util.scheduler.SchedulerWrapper;
```

2. Alle `Bukkit.getScheduler()` Aufrufe ersetzen mit entsprechenden `SchedulerWrapper` Methoden:

| Alter Code | Neuer Code | Anwendungsfall |
|-----------|------------|----------------|
| `Bukkit.getScheduler().runTask(plugin, task)` | `SchedulerWrapper.runGlobalTask(plugin, task)` | Globale Tasks |
| `Bukkit.getScheduler().runTaskLater(plugin, task, delay)` | `SchedulerWrapper.runTaskLater(plugin, entity, task, delay)` | Entity-Tasks mit Delay |
| `Bukkit.getScheduler().runTaskLater(plugin, task, delay)` | `SchedulerWrapper.runGlobalTaskLater(plugin, task, delay)` | Globale Tasks mit Delay |
| `Bukkit.getScheduler().scheduleSyncRepeatingTask(...)` | `SchedulerWrapper.runGlobalTaskTimer(...)` | Repeating Tasks |
| `Bukkit.getScheduler().runTaskAsynchronously(...)` | `SchedulerWrapper.runTaskAsync(...)` | Async Tasks |
| `Bukkit.getScheduler().runTaskLaterAsynchronously(...)` | `SchedulerWrapper.runTaskLaterAsync(...)` | Async Delayed Tasks |
| `Bukkit.getScheduler().cancelTasks(plugin)` | `SchedulerWrapper.cancelTasks(plugin)` | Tasks abbrechen |

#### Dateien die aktualisiert werden müssen:

**Core:**
- `AkrofolisPlugin.java` 
  - Zeile mit `Bukkit.getScheduler().cancelTasks(this)` → `SchedulerWrapper.cancelTasks(this)`

**Player Modules:**
- `DoubleJump.java`
  - `Bukkit.getScheduler().runTaskLater(...)` → `SchedulerWrapper.runTaskLater(getPlugin(), player, ...)`

- `PlayerListener.java`
  - `Bukkit.getScheduler().scheduleSyncDelayedTask(...)` → `SchedulerWrapper.runTaskLater(getPlugin(), player, ...)`

**World Modules:**
- `WorldProtect.java`
  - `Bukkit.getScheduler().scheduleSyncDelayedTask(...)` → `SchedulerWrapper.runTaskLater(getPlugin(), player, ...)`

- `LobbySpawn.java`
  - `Bukkit.getScheduler().scheduleSyncDelayedTask(...)` → `SchedulerWrapper.runGlobalTaskLater(...)`

- `SongPlayerManager.java`
  - `Bukkit.getScheduler().runTaskLaterAsynchronously(...)` → `SchedulerWrapper.runTaskLaterAsync(...)`

**Hotbar:**
- `HotbarItem.java`
  - `Bukkit.getScheduler().scheduleSyncDelayedTask(...)` → `SchedulerWrapper.runTaskLater(getPlugin(), player, ...)`

**Commands:**
- `LobbyCommand.java`
  - `Bukkit.getScheduler().scheduleSyncDelayedTask(...)` → `SchedulerWrapper.runTaskLater(plugin, player, ...)`

- `FlyCommand.java`
  - `Bukkit.getScheduler().runTaskAsynchronously(...)` → `SchedulerWrapper.runTaskAsync(...)`

- `AkrofolisCommand.java`
  - `Bukkit.getScheduler().runTaskLaterAsynchronously(...)` → `SchedulerWrapper.runTaskLaterAsync(...)`

**Inventory:**
- `AbstractInventory.java`
  - `plugin.getServer().getScheduler().runTaskTimerAsynchronously(...)` → `SchedulerWrapper.runTaskTimerAsync(...)`

## Bereits fertig implementierte Module ✅

Diese Module wurden bereits vollständig auf Folia umgestellt:

- ✅ **ScoreboardManager** - `ScheduledTask` statt `int`, SchedulerWrapper verwendet
- ✅ **TablistManager** - `ScheduledTask` statt `int`, SchedulerWrapper verwendet  
- ✅ **NametagManager** - `ScheduledTask` statt `int`, SchedulerWrapper verwendet
- ✅ **BossBarBroadcast** - `ScheduledTask` statt `int`, SchedulerWrapper verwendet
- ✅ **AutoBroadcast** - `ScheduledTask` statt `int`, SchedulerWrapper verwendet
- ✅ **FightModeManager** - `Map<UUID, ScheduledTask>` statt `Map<UUID, Integer>`, SchedulerWrapper verwendet
- ✅ **HologramManager** - SchedulerWrapper verwendet

## Schnell-Anleitung für manuelle Fixes

### Schritt 1: Import hinzufügen
In jeder der oben genannten Dateien, füge nach den bestehenden Imports hinzu:
```java
import me.zetastormy.Akrofolis.util.scheduler.SchedulerWrapper;
```

### Schritt 2: Scheduler-Aufrufe ersetzen
Verwende Suchen & Ersetzen in deinem Editor:

**Für Player-bezogene Tasks:**
- Suche: `Bukkit.getScheduler().runTaskLater(getPlugin(), () ->`
- Ersetze: `SchedulerWrapper.runTaskLater(getPlugin(), player, () ->`
- Hinweis: Stelle sicher, dass `player` im Kontext verfügbar ist!

**Für globale Tasks:**
- Suche: `Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(),`
- Ersetze: `SchedulerWrapper.runGlobalTaskLater(getPlugin(),`

**Für async Tasks:**
- Suche: `Bukkit.getScheduler().runTaskAsynchronously(`
- Ersetze: `SchedulerWrapper.runTaskAsync(`

### Schritt 3: Build & Test
```bash
./gradlew clean build
```

Das generierte JAR sollte dann sowohl auf Paper als auch auf Folia funktionieren!

## Vorteile der Implementierung

1. **Automatische Erkennung**: Das Plugin erkennt zur Laufzeit ob es auf Paper oder Folia läuft
2. **Abwärtskompatibilität**: Funktioniert weiterhin auf Paper 1.21+
3. **Zukunftssicher**: Bereit für Folia's Thread-per-Region Architektur
4. **Keine Breaking Changes**: API bleibt gleich, nur interne Implementierung ändert sich

## Testing

### Auf Paper:
```bash
# Plugin sollte normal funktionieren
# Im Log sollte stehen: "Running on: Paper"
```

### Auf Folia:
```bash
# Plugin sollte mit regionbasierten Schedulern laufen
# Im Log sollte stehen: "Running on: Folia"
```

## Hinweise

- Der SchedulerWrapper verwendet automatisch die richtigen Scheduler für Folia
- Entity-bezogene Tasks laufen im Thread der Entity's Region
- Location-bezogene Tasks laufen im Thread der Location's Region  
- Globale Tasks können von jedem Thread aus laufen
- Async Tasks laufen wie gewohnt asynchron

## Support

Bei Problemen:
1. Prüfe dass alle Scheduler-Aufrufe durch SchedulerWrapper ersetzt wurden
2. Stelle sicher dass Entity/Player Variablen im Kontext verfügbar sind
3. Prüfe die Logs für "Running on: Paper/Folia" beim Start

---
Erstellt als Teil der Folia-Kompatibilitäts-Implementation für Akrofolis v1.9.3+

