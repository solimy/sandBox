FROM rust as builder
WORKDIR /usr/src/myapp
COPY . .
RUN cargo build --release
RUN cargo install --path .

FROM alpine:slim
# RUN apk update && apk install -y extra-runtime-dependencies
COPY --from=builder /usr/local/cargo/bin/sandbox /usr/local/bin/sandbox
CMD ["sandbox"]
