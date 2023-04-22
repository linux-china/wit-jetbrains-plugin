<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# WIT JetBrains plugin Changelog

## [Unreleased]

## [0.3.1] - 2023-04-22

### Fixed

- Bug fix for identifier naming uppercase problem

## [0.3.0] - 2023-04-21

### Added

- live templates: `interface` and `world`
- `include-item` support

### Fixed

- `%type` should be record's field name

## [0.2.0] - 2023-02-26

### Added

- Code format support(experimental)
- Type names completion for `pkg.package_name.interface.{}` and `self.interface.{}`
- Navigation support for `pkg.package_name.interface.{}` and `self.interface.{}`
- Brace matcher added
- Goto symbol support for interface names
- Documentation support for `/// `
- Code completion: keywords, built-in types
- Indent enter support
- Spell bundled dictionary for Wasm words

## [0.1.0] - 2023-02-22

### Added

- Syntax highlight
- code completion for `pkg.` and `self.`
- live templates: `func` to declare a function
- file template: `WIT File` to create a new WIT file
