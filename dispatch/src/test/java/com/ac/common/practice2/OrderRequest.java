package com.ac.common.practice2;

public record OrderRequest(
    long price,
    long quantity,
    Side side
) {
}
