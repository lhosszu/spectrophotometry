package com.uni.spectro.domain.pipeline.exception

class PipelineInvocationException : RuntimeException {
    constructor(message: String?) : super(message) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}
}