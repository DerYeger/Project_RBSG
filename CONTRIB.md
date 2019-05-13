## Naming Conventions
Classes & Stuff CamelCase, First Letter Upper Case
Methods, Variables & Stuff camelCase, first letter lower case

Resources Kebab-Case, because-hyphens-are-cool

Packagenames and other stuff, where hyphens are not allowed in snake_case :(


## Methods
#### For public and protected methods

Parameters should be declared as final.

Object parameters must have @Nullable or @NotNull annotations

Object return types must have @Nullable or @NotNull.

Required Dependencies must come before Parameters must come before optional dependencies.

~~~~
public String giveExampleForCoolShit(
    @NotNull final ClassType requiredDependency,
    final int someParameter,
    @Nullable final OtherClassType optionalDependency
) {
    // ...
}
~~~~

## Classes
Comment Classes with @author doc-block, when you add or modify something.

## GIT
* micro commits pls.
* feature branches must be called feature/{featurename} where featurename should begin with a jira issue id
* create pull request on develop
* if you want you own develop branch for additional stuff and ideas, call it devel/{name}
