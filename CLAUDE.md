# CLAUDE.md

## Code Style Rules

### No Inline Imports (FQCN 직접 사용 금지)
- 클래스를 사용할 때 반드시 상단에 `import` 문을 선언하고, 코드 본문에서는 단순 클래스명만 사용할 것
- `android.app.Activity`처럼 FQCN(Fully Qualified Class Name)을 코드 본문에 직접 작성하지 않을 것
- Bad: `(context as? android.app.Activity)?.finish()`
- Good: `import android.app.Activity` 후 `(context as? Activity)?.finish()`
