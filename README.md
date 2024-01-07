<a href="https://paypal.me/benckx/2">
<img src="https://img.shields.io/badge/Donate-PayPal-green.svg"/>
</a>

# About

When viewing Lego models on <a href="https://www.bricklink.com/">Bricklink</a>, it is possible to add all the necessary
parts to a "Wanted List" and then use this list to find online stores where you can easily purchase them. However, it
doesn't seem possible to automatically filter out the parts you already own (from the sets you already own) from those "
Wanted List".

So I wrote this quick script to do that modifies the "Wanted List" XML file by removing the parts you already own. You
can then re-upload this modified XML and purchase the parts from this new list instead.

Maybe there's already a more simple solution directly on <a href="https://www.bricklink.com/">Bricklink</a>, but I could
not find it (if you know the solution, feel free to open an issue). In any case I'll be able to do more custom things
with my parts lists this way.

# How To

## on Rebrickable

First add all the sets you own: go to "My LEGO" -> "My Sets Lists". Create a list and add your sets. This will
allow <a href="https://rebrickable.com/">Rebrickable</a> to create an inventory of all the parts you own. You can also
add individual parts.

You can then export your inventory from <a href="https://rebrickable.com/">Rebrickable</a> (go to "My LEGO" -> "All my
parts" -> "Export Parts" -> "BrickLink XML") and replace data/inventory.xml by the export result.

## on Bricklink

Go to your "Wanted List" and click "Download". Move the file under the data folder.

## In Code

Replace the wanted list file accordingly:

```kotlin
fun main() {
    InventoryFilterer().filterOutInventoryParts("data/wanted-razor-crest-75331.xml")
}
```

The script will output a modified version of the "Wanted List" under the folder data, where it subtracts the parts you
already own.

It will also output a CSV report under folder data with the following format:

| modification type | part id | color code | color             | qty needed | qty inventory | qty missing |
|-------------------|---------|------------|-------------------|------------|---------------|-------------|
| sufficient        | 18649   | 86         | Light Bluish Gray | 4          | 4             | 0           |
| sufficient        | 22484   | 85         | Dark Bluish Gray  | 1          | 2             | 0           |
| ...               | ...     | ...        | ...               | ...        | ...           | ...         |
| insufficient      | 10247   | 11         | Black             | 6          | 2             | 4           |

# Future

- Order/sort the result to sort out parts you have enough of and parts you don't have at all -> [DONE]
- Add colors with names instead of codes, for easier navigation (I'd need to encode the list
  at https://rebrickable.com/colors/ to an enum or a map)  -> [DONE]
- Output links of referenced parts, for easier navigation
- Create some kind of rule engine that allow for color substitution (e.g. "use dark grey if I don't have the part in
  black")
- Maybe a UI and an executable so it's user-friendly for everyone
