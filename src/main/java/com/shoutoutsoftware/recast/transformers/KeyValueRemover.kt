package com.shoutoutsoftware.recast.transformers

/**
 * Created on 07 October 2017
 * Copyright © ShoutOut Software. All rights reserved.
 */

class KeyValueRemover : Transformer {

    override fun transform(map: HashMap<String, Any?>, keyToAlter: String): HashMap<String, Any?> {
        val alteredMap = HashMap(map)
        alteredMap.remove(keyToAlter)
        return alteredMap
    }

}
