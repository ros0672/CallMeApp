# CallMeApp Architecture

## Modules

### :core:ui
- UI Components (Components)

### :core:domain
- Use Cases (buisness logic)
- Data models (Entity)
- Repository Interfaces

### :core:data
- Repository Implementations
- Retrofit API
- DataStore (tokens)

### :core:di
- Dagger Components
- DI modules

### :feature:auth
- Sign In/Sign Up Screens
- AuthViewModel

### :feature:contacts (future)

### :feature:calls (future)

### :feature:profile (future)

## Layers
- Data: Repository, DataStore, Retrofit API
- Domain: UseCases, Entities
- Presentation: ViewModel, Screens (Compose)

## Dependencies
- :core:data -> :core:domain
- core:di -> core:domain, core:data, :core:ui
- :feature:auth -> core:domain, :core:ui
- :app -> :core:di, :core:data, :feature:auth