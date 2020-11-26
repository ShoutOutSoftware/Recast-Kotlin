package com.shoutoutsoftware.recast.transformers

class KeyValueRemover : Transformer {

    override fun transform(map: HashMap<String, Any?>, keyToAlter: String): HashMap<String, Any?> {
        val alteredMap = HashMap(map)
        alteredMap.remove(keyToAlter)
        return alteredMap
    }

}
