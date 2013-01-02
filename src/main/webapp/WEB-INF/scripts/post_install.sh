#! /bin/bash
# GMX platform post-install for GSC-enabled web applications
#
# $Id: post_install.sh 34449 2011-11-14 10:59:25Z jhe $

#
# HERE BE DRAGONS!
#

# Human in need of help?
case "$1" in
    -h | -\? | --help)
        echo "Usage: $0 [-n] [-v] [<system env file>]"
        exit 1
    ;;
esac

# Helper functions
clean_config() {
    # Since the calling infrastructure totally ignores any return codes
    # of this script for whatever reasons, remove any existing configuration 
    # files so that the webapp will _likely_ fail on startup.
    $TRACE "Cleaning existing config..."
    for i in $(find $WEBAPP_DIR -name "*.template.*" -o -name "*.empy.*"); do
        cfgfile="$(sed -e 's/\.template\././g' -e 's/\.empy\././g' <<<$i)"
        test -f "$cfgfile" && $DRY_RUN rm -f "$cfgfile"
    done
}
fail() {
    echo >&2 "FATAL:" "$@"
    clean_config
    exit 1
}
trace() {
    echo >&2 "TRACE:" "$@"
}

# Check command line arguments
DRY_RUN=
if test "$1" = "-n"; then
    DRY_RUN=echo
    shift
fi
export TRACE=:
CMD_OPTS=""
if test "$1" = "-v"; then
    export TRACE=trace
    CMD_OPTS="$CMD_OPTS -v"
    shift
fi
ENV_FILE=${1:-/opt/gmx/etc/gmxsys.env}

# Look where we are located & remove current config
WEBAPP_DIR=$(cd $(dirname $0)/../.. && pwd)
clean_config

# Load host environment
. ${ENV_FILE}
echo "post-installing ${WEBAPP_DIR} @ $LONGHOST_INT"

# Define Title, Vendor & Version based on MANIFEST.MF
eval $(grep Implementation $WEBAPP_DIR/META-INF/MANIFEST.MF | tr -d \\r \
    | sed -e s/Implementation-/MF_/ -e 's/: /="/' -e 's/$/"/' | egrep '^[_a-zA-Z0-9]+="')
$TRACE "MANIFEST info:" $MF_Title, $MF_Version, $MF_Vendor
test -z "$MF_Title" && fail "Bad MANIFEST.MF (no application name defined)"
test -z "$MF_Version" && fail "Bad MANIFEST.MF (no version defined)"

# Collect property files; tries to find configuration files that are named
# like the web application or the actual webapp context, without or with 
# version number; versioned files take precedence
property_opts=""
all_env=""
for envname in $(echo $MF_Title $MF_Title-$MF_Version \
                      $(basename $WEBAPP_DIR | sed -e 's/-[0-9].*$//g') \
                      $(basename $WEBAPP_DIR | sed -e 's/\.war$//g') \
                 | tr " " \\n | sort | uniq) ; do
    for extension in env properties; do
        envfile=$BASE/etc/$envname.$extension
        all_env="$all_env $envfile"
        $TRACE Checking $envfile
        if test -f "$envfile"; then
            property_opts="$property_opts -p $envfile"
        fi
    done
done
test -z "$property_opts" && fail "No configuration files found, looked for:$all_env"
property_opts="-p $ENV_FILE$property_opts"

# Make it so
cmd="$BASE/bin/webapp-inject $CMD_OPTS $property_opts ${WEBAPP_DIR}"
$TRACE Calling $cmd
$DRY_RUN $cmd || fail "$MF_Title configuration failed"

