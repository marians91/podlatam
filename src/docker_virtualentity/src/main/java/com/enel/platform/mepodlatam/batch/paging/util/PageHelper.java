package com.enel.platform.mepodlatam.batch.paging.util;

import com.enel.platform.entity.utils.batch.SegmentsHelper;

/**
 * Specific Pod PageHelper.
 */
public interface PageHelper<T> extends SegmentsHelper {

    /**
     * Return the page size.
     */
    int getPageSize();
    
    int segmentsNumber(long recordsCount);
}
