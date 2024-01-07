<a href="https://paypal.me/benckx/2">
<img src="https://img.shields.io/badge/Donate-PayPal-green.svg"/>
</a>

# About

When viewing Lego models on <a href="https://www.bricklink.com/">Bricklink</a>, it is possible to add all the necessary
parts to a "Wanted List" and then to use this list to find online stores where you can purchase them. However, it
doesn't seem possible to automatically subtract the parts you already own (from the sets you already own).

So I wrote this quick script to do that.

Maybe there's already a more simple solution directly on <a href="https://www.bricklink.com/">Bricklink</a>, but I could
not find it (if you know the solution, feel free to open an issue). In any case I'll be able to do more custom things
with my parts lists this way.

# How To

## on Rebrickable

First add all the sets you own: go to "My LEGO" -> "My Sets Lists". Create a list and add your sets. This will
allow <a href="https://rebrickable.com/">Rebrickable</a> to create an inventory of all the parts you own. You can also
add individual parts.

You can then export your inventory from <a href="https://rebrickable.com/">Rebrickable</a> (go to "My LEGO" -> "All my
parts" -> "Export Parts" -> "BrickLink XML") and replace the "inventory.xml" file by the export result.

## on Bricklink

Go to your "Wanted List" and click "Download".

## In Code

Replace the files accordingly:

```kotlin
fun main() {
    val mapper = ItemMapper()
    val inventory = mapper.parseItems("inventory.xml")
    val wanted = mapper.parseItems("wanted-razor-crest-75331.xml")
    ItemComparator.analyze(inventory, wanted)
}
```

Then result will show as:

```
[...]
you already have the part, but in insufficient quantity -> Item(itemId=24246, color=1, minQty=4) (you have 2 but need 4)
you already have the part, but in insufficient quantity -> Item(itemId=23443, color=86, minQty=6) (you have 2 but need 6)
you already have the part, but in insufficient quantity -> Item(itemId=26047, color=11, minQty=3) (you have 1 but need 3)
you already have the part, but in insufficient quantity -> Item(itemId=85984pb127, color=85, minQty=5) (you have 3 but need 5)
you already have the part in quantity -> Item(itemId=34103, color=86, minQty=4) (you have 8 and you need 4)
you already have the part in quantity -> Item(itemId=32828, color=86, minQty=4) (you have 9 and you need 4)
you already have the part, but in insufficient quantity -> Item(itemId=36840, color=85, minQty=6) (you have 3 but need 6)
you already have the part, but in insufficient quantity -> Item(itemId=2420, color=86, minQty=11) (you have 4 but need 11)
you already have the part, but in insufficient quantity -> Item(itemId=2431, color=86, minQty=56) (you have 3 but need 56)
[...]
```

# Future

- Order/sort the result to sort out parts you have enough of and parts you don't have at all
- Add colors with names instead of codes, for easier navigation (I'd need to encode the list at https://rebrickable.com/colors/ to an enum or a map)
- Output links of referenced parts, for easier navigation
- Create some kind of rule engine that allow for color substitution (e.g. "use dark grey if I don't have the part in
  black")
- Maybe a UI and an executable so it's user-friendly for everyone
