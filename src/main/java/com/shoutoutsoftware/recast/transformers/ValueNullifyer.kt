package com.shoutoutsoftware.recast.transformers

class ValueNullifyer : Transformer {

    override fun transform(map: HashMap<String, Any?>, keyToAlter: String): HashMap<String, Any?> {
        val alteredMap = HashMap(map)
        alteredMap[keyToAlter] = null
        return alteredMap
    }

}
