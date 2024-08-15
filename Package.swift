// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "SmsRetriever",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "SmsRetriever",
            targets: ["SmsRetrieverPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "SmsRetrieverPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/SmsRetrieverPlugin"),
        .testTarget(
            name: "SmsRetrieverPluginTests",
            dependencies: ["SmsRetrieverPlugin"],
            path: "ios/Tests/SmsRetrieverPluginTests")
    ]
)