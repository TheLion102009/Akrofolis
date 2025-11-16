# AkroFolis - Naming Convention

## ✅ Was wurde geändert

### Plugin-Name (für Benutzer sichtbar)
- **Plugin-Name:** `AkroFolis` (mit großem F)
- **JAR-Datei:** `Akrofolis-1.9.3.jar`
- **Command:** `/akrofolis` (Alias: `/akrofo`)
- **Permissions:** `akrofolis.*`

### Was NICHT geändert wurde (intern)

Die **Java-Package- und Klassennamen** bleiben unverändert:
- ✅ `me.zetastormy.akropolis.AkropolisPlugin`
- ✅ `me.zetastormy.akropolis.AkropolisPluginLoader`
- ✅ Alle anderen Package-Namen: `me.zetastormy.akropolis.*`

**Warum?** 
- Package-Umbenennungen würden alle Imports brechen
- Klassennamen-Änderungen würden externe Referenzen brechen
- Die internen Namen sind für Benutzer nicht sichtbar

## 📝 paper-plugin.yml Konfiguration

```yaml
name: AkroFolis                                        # ← Benutzer sieht diesen Namen
main: me.zetastormy.akropolis.AkropolisPlugin        # ← Interner Klassenname (nicht ändern!)
loader: me.zetastormy.akropolis.AkropolisPluginLoader # ← Interner Klassenname (nicht ändern!)

permissions:
  akrofolis.*:                                         # ← Benutzer verwendet diese Permissions
```

## 🎮 Für Benutzer

### Commands:
```
/akrofolis help
/akrofolis reload
/akrofo help      (Alias)
```

### Permissions:
```yaml
akrofolis.*
akrofolis.command.*
akrofolis.command.help
akrofolis.bypass.*
```

### Plugin-Info:
```
Name: AkroFolis
Version: 1.9.3
Authors: ZetaStormy, ItsLewizzz, Thelion102009
```

## 🔧 Für Entwickler

### Java Code bleibt unverändert:
```java
package me.zetastormy.akropolis;

public class AkropolisPlugin extends JavaPlugin {
    // Code...
}
```

### Imports bleiben gleich:
```java
import me.zetastormy.akropolis.AkropolisPlugin;
import me.zetastormy.akropolis.module.ModuleManager;
// etc.
```

## ✅ Zusammenfassung

| Element | Wert | Änderbar? |
|---------|------|-----------|
| Plugin-Name | AkroFolis | ✅ Ja |
| JAR-Datei | Akrofolis-1.9.3.jar | ✅ Ja |
| Command | /akrofolis | ✅ Ja |
| Permissions | akrofolis.* | ✅ Ja |
| Package | me.zetastormy.akropolis | ❌ Nein |
| Klassen | AkropolisPlugin, etc. | ❌ Nein |

**Grund:** Nur benutzer-sichtbare Namen wurden geändert. Die interne Java-Struktur bleibt stabil.

