package com.shoutoutsoftware.recast.transformers

interface Transformer {
    fun transform(map: HashMap<String, Any?>, keyToAlter: String): HashMap<String, Any?>
}
