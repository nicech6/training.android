package com.cuh.inject.annotation;/*
 *
 * Copyright (C) 2022 NIO Inc
 *
 * Ver   Date        Author    Desc
 *
 * V1.0  2022/5/11  hai.cui  Add for
 *
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.CLASS)
public @interface Debounced {
}
