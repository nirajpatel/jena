/*
 * (c) Copyright 2008 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.tdb.pgraph;

import static com.hp.hpl.jena.tdb.Const.IndexRecordLength;
import static com.hp.hpl.jena.tdb.Const.NodeKeyLength;
import static com.hp.hpl.jena.tdb.Const.NodeValueLength;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.tdb.base.file.Location;
import com.hp.hpl.jena.tdb.base.record.RecordFactory;
import com.hp.hpl.jena.tdb.index.Index;
import com.hp.hpl.jena.tdb.index.IndexBuilder;
import com.hp.hpl.jena.tdb.index.RangeIndex;
import com.hp.hpl.jena.tdb.index.TripleIndex;

/** Place to put various "making" things. */

public class PGraphFactory
{
    static Logger log = LoggerFactory.getLogger(PGraphBase.class) ;

    // ---- Record factories
    public final static RecordFactory indexRecordFactory = new RecordFactory(IndexRecordLength, 0) ; 
    public final static RecordFactory nodeRecordFactory = new RecordFactory(NodeKeyLength, NodeValueLength) ;
    
    /** Create a graph backed with storage at a particular location using a system of indexes */
    public static PGraphBase create(IndexBuilder factory, Location location)
    {
        if ( location == null )
            log.warn("Null location") ;
        
        RangeIndex idxSPO = factory.newRangeIndex(location, indexRecordFactory, "SPO") ;
        TripleIndex triplesSPO = new TripleIndex("SPO", idxSPO) ;

        RangeIndex idxPOS = factory.newRangeIndex(location, indexRecordFactory, "POS") ;
        TripleIndex triplesPOS = new TripleIndex("POS", idxPOS) ;

        RangeIndex idxOSP = factory.newRangeIndex(location, indexRecordFactory, "OSP") ;
        TripleIndex triplesOSP = new TripleIndex("OSP", idxOSP) ;
     
        NodeTable nodeTable = new NodeTableIndex(factory, location) ;

        return new PGraphBase(triplesSPO, triplesPOS, triplesOSP, nodeTable) ;
    }
    
    // ----
    
    /** Create a graph backed with storage at a particular location */
    public static PGraphBase create(Location location)  { return create(IndexBuilder.get(), location) ; }
    
    /** Create a graph backed with storage in-memory (maily for testing) */
    public static PGraphBase createMem()
    {
        return createMem(IndexBuilder.mem()) ;
    }
    
    /** Create a graph backed with storage in-memory (maily for testing) */
    public static PGraphBase createMem(IndexBuilder factory)
    { 
        RangeIndex idxSPO = factory.newRangeIndex(null, indexRecordFactory, "SPO") ;
        TripleIndex triplesSPO = new TripleIndex("SPO", idxSPO) ;

        RangeIndex idxPOS = factory.newRangeIndex(null, indexRecordFactory, "POS") ;
        TripleIndex triplesPOS = new TripleIndex("POS", idxPOS) ;

        RangeIndex idxOSP = factory.newRangeIndex(null, indexRecordFactory, "OSP") ;
        TripleIndex triplesOSP = new TripleIndex("OSP", idxOSP) ;
     
        Index nodeIndex = factory.newIndex(null, nodeRecordFactory, "node2id") ;
        
        NodeTable nodeTable = new NodeTableIndex(factory) ;
        
        return new PGraphBase(triplesSPO, triplesPOS, triplesOSP, nodeTable) ;
    }
    
    
}

/*
 * (c) Copyright 2008 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */